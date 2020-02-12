package com.guardian.core.mediaplayer.podx

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.isPlaying
import com.guardian.core.podxevent.PodXEventRepository
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXSupportEvent
import com.guardian.core.podxevent.PodXWebEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.max

class PodXEventEmitterImpl
@Inject constructor(
    private val mediaSessionConnection: MediaSessionConnection,
    private val podXEventRepository: PodXEventRepository
) :
    PodXEventEmitter {

    private val currentFeedDisposable = CompositeDisposable()
    private val currentFeedItemDisposable = CompositeDisposable()

    init {
        mediaSessionConnection.playbackState.observeForever {
            registerPlaybackTimerObservable()
        }
    }

    private val podXImageEventMutableLiveData = MutableLiveData<List<PodXImageEvent>>()
        .apply {
            value = listOf()
        }
    override val podXImageEventLiveData: LiveData<List<PodXImageEvent>> =
        podXImageEventMutableLiveData

    private val podXWebEventMutableLiveData = MutableLiveData<List<PodXWebEvent>>()
        .apply {
            value = listOf()
        }
    override val podXWebEventLiveData: LiveData<List<PodXWebEvent>> = podXWebEventMutableLiveData

    private val podXSupportEventMutableLiveData = MutableLiveData<List<PodXSupportEvent>>()
        .apply {
            value = listOf()
        }
    override val podXSupportEventLiveData: LiveData<List<PodXSupportEvent>> =
        podXSupportEventMutableLiveData

    private val pendingPodXImageEvents: MutableList<PodXImageEvent> = mutableListOf()
    private val pendingPodXWebEvents: MutableList<PodXWebEvent> = mutableListOf()
    private val pendingPodXSupportEvents: MutableList<PodXSupportEvent> = mutableListOf()

    override fun registerCurrentFeedItem(feedItem: FeedItem) {
        currentFeedDisposable.clear()

        podXImageEventMutableLiveData.postValue(listOf())
        podXWebEventMutableLiveData.postValue(listOf())
        podXSupportEventMutableLiveData.postValue(listOf())

        registerImageEvents(feedItem)
        registerWebEvents(feedItem)
        registerSupportEvents(feedItem)
    }

    private fun registerWebEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getWebEventsForFeedItem(feedItem)
                .subscribe({ feedPodXEventList ->
                    pendingPodXWebEvents.clear()
                    pendingPodXWebEvents.addAll(feedPodXEventList)
                    registerPlaybackTimerObservable()
                }, { e: Throwable ->
                    Timber.e(e)
                })
        )
    }

    private fun registerImageEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getImageEventsForFeedItem(feedItem)
                .subscribe({ feedPodXEventList ->
                    pendingPodXImageEvents.clear()
                    pendingPodXImageEvents.addAll(feedPodXEventList)
                    registerPlaybackTimerObservable()
                }, { e: Throwable ->
                    Timber.e(e)
                })
        )
    }

    private fun registerSupportEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getSupportEventsForFeedItem(feedItem)
                .subscribe({ feedPodXEventList ->
                    pendingPodXSupportEvents.clear()
                    pendingPodXSupportEvents.addAll(feedPodXEventList)
                    registerPlaybackTimerObservable()
                }, { e: Throwable ->
                    Timber.e(e)
                })
        )
    }

    private fun registerPlaybackTimerObservable() {
        currentFeedItemDisposable.clear()
        val playbackState = mediaSessionConnection.playbackState.value
        if (playbackState != null && playbackState.isPlaying) {
            updateEmittedEvents(getPlaybackPositionFromState(playbackState))

            val nextEventTime = getNextEventTime(getPlaybackPositionFromState(playbackState))
            val nextEventObservable = Observable.fromCallable {
                mediaSessionConnection.playbackState.value.let { playbackState ->
                    if (playbackState != null) {
                        getPlaybackPositionFromState(playbackState)
                    } else {
                        0L
                    }
                }
            }.delaySubscription (
                nextEventTime, TimeUnit.MILLISECONDS
            )

            currentFeedItemDisposable.add(
                nextEventObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ timeMillis ->
                        registerPlaybackTimerObservable()
                    }, { e: Throwable? ->
                        Timber.e(e)
                    })
            )
        }
    }

    /**
     * This function assumes there is at least one podx event in the future for this feed item
     */
    private fun getNextEventTime(currentTimeMillis: Long): Long {
        val imageMin = pendingPodXImageEvents.asSequence()
            .filter { event ->
                event.timeStart > currentTimeMillis || event.timeEnd > currentTimeMillis
            }
            .map { event ->
                if (event.timeStart > currentTimeMillis) {
                    event.timeStart - currentTimeMillis
                } else {
                    event.timeEnd - currentTimeMillis
                }
            }.min()

        val webMin = pendingPodXWebEvents.asSequence()
            .filter { event ->
                event.timeStart > currentTimeMillis || event.timeEnd > currentTimeMillis
            }
            .map { event ->
                if (event.timeStart > currentTimeMillis) {
                    event.timeStart - currentTimeMillis
                } else {
                    event.timeEnd - currentTimeMillis
                }
            }.min()

        val supportMin = pendingPodXSupportEvents.asSequence()
            .filter { event ->
                event.timeStart > currentTimeMillis || event.timeEnd > currentTimeMillis
            }
            .map { event ->
                if (event.timeStart > currentTimeMillis) {
                    event.timeStart - currentTimeMillis
                } else {
                    event.timeEnd - currentTimeMillis
                }
            }.min()

        val minEvent = listOf(imageMin, webMin, supportMin).minBy {
            it ?: Long.MAX_VALUE
        } ?: Long.MAX_VALUE

        return max (minEvent, 250L)
    }

    private fun updateEmittedEvents(timeMillis: Long) {
        val currentImageEventList = pendingPodXImageEvents
            .filter { pendingImageEvent ->
                pendingImageEvent.timeStart <= timeMillis &&
                    pendingImageEvent.timeEnd >= timeMillis
            }

        // only post if there are new values
        if (currentImageEventList
                .size != (podXImageEventMutableLiveData.value)?.size ?: 0) {
            podXImageEventMutableLiveData.postValue(currentImageEventList)
        }

        val currentWebEventList = pendingPodXWebEvents
            .filter { pendingWebEvent ->
                pendingWebEvent.timeStart <= timeMillis &&
                    pendingWebEvent.timeEnd >= timeMillis
            }
        if (currentWebEventList
                .size != (podXWebEventMutableLiveData.value)?.size ?: 0) {
            podXWebEventMutableLiveData.postValue(currentWebEventList)
        }

        val currentSupportEventList = pendingPodXSupportEvents
            .filter { pendingSupportEvent ->
                pendingSupportEvent.timeStart <= timeMillis &&
                    pendingSupportEvent.timeEnd >= timeMillis
            }
        podXSupportEventMutableLiveData.postValue(currentSupportEventList)
        if (currentSupportEventList
                .size != (podXSupportEventMutableLiveData.value)?.size ?: 0) {
            podXSupportEventMutableLiveData.postValue(currentSupportEventList)
        }

    }

    private fun getPlaybackPositionFromState(playbackStateCompat: PlaybackStateCompat): Long {
        return if (playbackStateCompat.state == PlaybackStateCompat.STATE_PLAYING) {
            val timeDelta = SystemClock.elapsedRealtime() -
                playbackStateCompat.lastPositionUpdateTime

            (playbackStateCompat.position + (timeDelta * playbackStateCompat.playbackSpeed))
                .toLong()
        } else {
            playbackStateCompat.position
        }
    }
}
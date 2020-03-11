package com.guardian.core.mediaplayer.podx

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.isPlaying
import com.guardian.core.podxevent.PodXCallPromptEvent
import com.guardian.core.podxevent.PodXEventRepository
import com.guardian.core.podxevent.PodXFeedBackEvent
import com.guardian.core.podxevent.PodXFeedLinkEvent
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXNewsLetterSignUpEvent
import com.guardian.core.podxevent.PodXPollEvent
import com.guardian.core.podxevent.PodXSocialPromptEvent
import com.guardian.core.podxevent.PodXSupportEvent
import com.guardian.core.podxevent.PodXTextEvent
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

    private val podXCallPromptEventMutableLiveData = MutableLiveData<List<PodXCallPromptEvent>>()
        .apply {
            value = listOf()
        }
    override val podXCallPromptEventLiveData: LiveData<List<PodXCallPromptEvent>> =
        podXCallPromptEventMutableLiveData

    private val podXFeedBackEventMutableLiveData = MutableLiveData<List<PodXFeedBackEvent>>()
        .apply {
            value = listOf()
        }
    override val podXFeedBackEventLiveData: LiveData<List<PodXFeedBackEvent>> =
        podXFeedBackEventMutableLiveData

    private val podXFeedLinkEventMutableLiveData = MutableLiveData<List<PodXFeedLinkEvent>>()
        .apply {
            value = listOf()
        }
    override val podXFeedLinkEventLiveData: LiveData<List<PodXFeedLinkEvent>> =
        podXFeedLinkEventMutableLiveData

    private val podXNewsLetterSignUpEventMutableLiveData = MutableLiveData<List<PodXNewsLetterSignUpEvent>>()
        .apply {
            value = listOf()
        }
    override val podXNewsLetterSignUpEventLiveData: LiveData<List<PodXNewsLetterSignUpEvent>> =
        podXNewsLetterSignUpEventMutableLiveData

    private val podXPollEventMutableLiveData = MutableLiveData<List<PodXPollEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPollEventLiveData: LiveData<List<PodXPollEvent>> =
        podXPollEventMutableLiveData

    private val podXSocialPromptEventMutableLiveData = MutableLiveData<List<PodXSocialPromptEvent>>()
        .apply {
            value = listOf()
        }
    override val podXSocialPromptEventLiveData: LiveData<List<PodXSocialPromptEvent>> =
        podXSocialPromptEventMutableLiveData

    private val podXTextEventMutableLiveData = MutableLiveData<List<PodXTextEvent>>()
        .apply {
            value = listOf()
        }
    override val podXTextEventLiveData: LiveData<List<PodXTextEvent>> =
        podXTextEventMutableLiveData

    private val pendingPodXImageEvents: MutableList<PodXImageEvent> = mutableListOf()
    private val pendingPodXWebEvents: MutableList<PodXWebEvent> = mutableListOf()
    private val pendingPodXSupportEvents: MutableList<PodXSupportEvent> = mutableListOf()
    private val pendingPodXCallPromptEvents: MutableList<PodXCallPromptEvent> = mutableListOf()
    private val pendingPodXFeedBackEvents: MutableList<PodXFeedBackEvent> = mutableListOf()
    private val pendingPodXFeedLinkEvents: MutableList<PodXFeedLinkEvent> = mutableListOf()
    private val pendingPodXNewsLetterSignUpEvents: MutableList<PodXNewsLetterSignUpEvent> = mutableListOf()
    private val pendingPodXPollEvents: MutableList<PodXPollEvent> = mutableListOf()
    private val pendingPodXSocialPromptEvents: MutableList<PodXSocialPromptEvent> = mutableListOf()
    private val pendingPodXTextEvents: MutableList<PodXTextEvent> = mutableListOf()

    private var pendingPodXEventTimes: List<Long> = listOf()

    override fun registerCurrentFeedItem(feedItem: FeedItem) {
        currentFeedDisposable.clear()

        podXImageEventMutableLiveData.postValue(listOf())
        podXWebEventMutableLiveData.postValue(listOf())
        podXSupportEventMutableLiveData.postValue(listOf())

        registerImageEvents(feedItem)
        registerWebEvents(feedItem)
        registerSupportEvents(feedItem)

        setupPendingEventTimes()
    }

    private fun setupPendingEventTimes() {
        pendingPodXEventTimes = pendingPodXImageEvents.flatMap {
            listOf(it.timeStart, it.timeEnd)
        } + pendingPodXCallPromptEvents.flatMap {
            listOf(it.timeStart, it.timeEnd)
        } + pendingPodXFeedBackEvents.flatMap {
            listOf(it.timeStart, it.timeEnd)
        } + pendingPodXFeedLinkEvents.flatMap {
            listOf(it.timeStart, it.timeEnd)
        } + pendingPodXNewsLetterSignUpEvents.flatMap {
            listOf(it.timeStart, it.timeEnd)
        } + pendingPodXPollEvents.flatMap {
            listOf(it.timeStart, it.timeEnd)
        } + pendingPodXSocialPromptEvents.flatMap {
            listOf(it.timeStart, it.timeEnd)
        } + pendingPodXSupportEvents.flatMap {
            listOf(it.timeStart, it.timeEnd)
        } + pendingPodXTextEvents.flatMap {
            listOf(it.timeStart, it.timeEnd)
        } + pendingPodXWebEvents.flatMap {
            listOf(it.timeStart, it.timeEnd)
        }
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
    
    private fun getNextEventTime(currentTimeMillis: Long): Long {
        val minEvent = pendingPodXEventTimes.filter{ eventTime -> eventTime > currentTimeMillis }
            .min() ?: Long.MAX_VALUE - currentTimeMillis

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
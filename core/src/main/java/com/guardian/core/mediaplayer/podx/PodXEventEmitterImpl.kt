package com.guardian.core.mediaplayer.podx

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.podxevent.PodXEventRepository
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXWebEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PodXEventEmitterImpl
@Inject constructor(
    private val mediaSessionConnection: MediaSessionConnection,
    private val podXEventRepository: PodXEventRepository
) :
    PodXEventEmitter {

    // playback timer is started after we have gotten a valid podx event list from the repo so that
    // a concurrent modification exception can be avoided
    private var playbackTimerDisposable = Disposables.empty()

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

    private val pendingPodXImageEvents: MutableList<PodXImageEvent> = mutableListOf()
    private val pendingPodXWebEvents: MutableList<PodXWebEvent> = mutableListOf()

    private val currentFeedDisposable = CompositeDisposable()
    override fun registerCurrentFeedItem(feedItem: FeedItem) {
        currentFeedDisposable.clear()

        podXImageEventMutableLiveData.postValue(listOf())
        podXWebEventMutableLiveData.postValue(listOf())

        registerImageEvents(feedItem)
        registerWebEvents(feedItem)
        registerPlaybackTimerObservable()
    }

    private fun registerWebEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getWebEventsForFeedItem(feedItem)
                .subscribe({ feedPodXEventList ->
                    pendingPodXWebEvents.clear()
                    pendingPodXWebEvents.addAll(feedPodXEventList)
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
                }, { e: Throwable ->
                    Timber.e(e)
                })
        )
    }

    private fun registerPlaybackTimerObservable() {
        val timerObservable = Observable.interval(
            500, TimeUnit.MILLISECONDS
        ).filter { mediaSessionConnection.playbackState.value != null &&
            mediaSessionConnection.playbackState.value?.state == PlaybackStateCompat.STATE_PLAYING
        }.map {
            mediaSessionConnection.playbackState.value.let { playbackState ->
                if (playbackState != null) {
                    getPlaybackPositionFromState(playbackState)
                } else {
                    0L
                }
            }
        }

        currentFeedDisposable.add(
            timerObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ timeMillis ->
                    val currentImageEventList = pendingPodXImageEvents.filter { pendingImageEvent ->
                        pendingImageEvent.timeStart < timeMillis &&
                            pendingImageEvent.timeEnd > timeMillis
                    }
                    // only post if there are new values
                    if (currentImageEventList
                            .intersect(podXImageEventMutableLiveData.value ?: listOf())
                            .size != currentImageEventList.size) {
                        podXImageEventMutableLiveData.postValue(currentImageEventList)
                    }

                    val currentWebEventList = pendingPodXWebEvents.filter { pendingWebEvent ->
                        pendingWebEvent.timeStart < timeMillis &&
                            pendingWebEvent.timeEnd > timeMillis
                    }

                    // only post if there are new values
                    if (currentWebEventList
                            .intersect(podXWebEventMutableLiveData.value ?: listOf())
                            .size != currentWebEventList.size) {
                        podXWebEventMutableLiveData.postValue(currentWebEventList)
                    }
                }, { e: Throwable? ->
                    Timber.e(e)
                })
        )
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
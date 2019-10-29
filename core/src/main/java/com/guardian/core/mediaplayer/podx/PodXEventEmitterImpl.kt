package com.guardian.core.mediaplayer.podx

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.library.subscribeOnIoObserveOnIo
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.common.NOTHING_PLAYING
import com.guardian.core.podxevent.PodXEvent
import com.guardian.core.podxevent.dao.PodXEventDao
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import timber.log.Timber
import java.util.PriorityQueue
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PodXEventEmitterImpl
@Inject constructor(
    private val mediaSessionConnection: MediaSessionConnection,
    private val podXEventDao: PodXEventDao
) :
    PodXEventEmitter {

    private var playbackTimerDisposable = Disposables.empty()
    init {
        mediaSessionConnection.nowPlaying.observeForever { mediaMetadataCompat ->
            playbackTimerDisposable.dispose()
            if (mediaMetadataCompat != null &&
                mediaMetadataCompat != NOTHING_PLAYING) {
                // something is playing
                playbackTimerDisposable = registerPlaybackTimerObservable()
            }
        }
    }

    private val podXEventMutableLiveData = MutableLiveData<PodXEvent>()
    override val podXEventLiveData: LiveData<PodXEvent> = podXEventMutableLiveData

    private val podXEventQueue: PriorityQueue<PodXEvent> = PriorityQueue(30) { o1, o2 ->
            (o1.timeStart - o2.timeStart).toInt()
        }

    private var currentFeedDisposable = Disposables.empty()
    override fun registerCurrentFeedItem(feedItem: FeedItem) {
        currentFeedDisposable.dispose()
        currentFeedDisposable = podXEventDao.getPodXEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
            .subscribeOnIoObserveOnIo()
            .subscribe({ feedPodXEventList ->
                podXEventQueue.clear()
                podXEventQueue.addAll(feedPodXEventList)

                // trim events that have already been shown
                val currentState = mediaSessionConnection.playbackState.value
                if (currentState != null) {
                    val currentTime = getPlaybackPositionFromState(currentState)
                    while (podXEventQueue.peek()?.timeStart ?: -1 < currentTime) {
                        podXEventQueue.poll()
                    }
                }
            }, { e: Throwable ->
                Timber.e(e)
            })
    }

    private fun registerPlaybackTimerObservable(): Disposable {
        val timerObservable = Observable.interval(
            100, TimeUnit.MILLISECONDS
        ).map {
            mediaSessionConnection.playbackState.value.let { playbackState ->
                if (playbackState != null) {
                    getPlaybackPositionFromState(playbackState)
                } else {
                    0L
                }
            }
        }

        return timerObservable.subscribe({ timeMillis ->
            val nextEvent = podXEventQueue.peek()
            if (nextEvent != null &&
                nextEvent.timeStart < timeMillis) {
                podXEventMutableLiveData.postValue(podXEventQueue.poll())
            }
        },
            { e: Throwable? -> Timber.e(e) }
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
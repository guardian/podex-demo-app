package com.guardian.core.mediaplayer.podx

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.mediaplayer.common.MediaSessionConnection
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

    // playback timer is started after we have gotten a valid podx event list from the repo so that
    // a concurrent modification exception can be avoided
    private var playbackTimerDisposable = Disposables.empty()

    private val podXEventMutableLiveData = MutableLiveData<List<PodXEvent>>()
        .apply {
            value = listOf()
        }

    override val podXEventLiveData: LiveData<List<PodXEvent>> = podXEventMutableLiveData

    private val podXEventQueue: PriorityQueue<PodXEvent> = PriorityQueue(30) { o1, o2 ->
            (o1.timeStart - o2.timeStart).toInt()
        }

    private var currentFeedDisposable = Disposables.empty()
    override fun registerCurrentFeedItem(feedItem: FeedItem) {
        currentFeedDisposable.dispose()
        podXEventMutableLiveData.postValue(listOf())
        currentFeedDisposable = podXEventDao.getPodXEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
            .subscribe({ feedPodXEventList ->
                playbackTimerDisposable.dispose()
                podXEventQueue.clear()
                podXEventQueue.addAll(feedPodXEventList)

                // trim events that have already been shown in case we are registering a feed item
                // that is already part way through playback.
                val currentState = mediaSessionConnection.playbackState.value
                if (currentState != null) {
                    val currentTime = getPlaybackPositionFromState(currentState)
                    while (podXEventQueue.peek()?.timeStart ?: -1 < currentTime) {
                        podXEventQueue.poll()
                    }
                }
                podXEventMutableLiveData.postValue(listOf())
                playbackTimerDisposable = registerPlaybackTimerObservable()
            }, { e: Throwable ->
                Timber.e(e)
            })


    }

    private fun registerPlaybackTimerObservable(): Disposable {
        val timerObservable = Observable.interval(
            500, TimeUnit.MILLISECONDS
        ).map {
            mediaSessionConnection.playbackState.value.let { playbackState ->
                if (playbackState != null) {
                    getPlaybackPositionFromState(playbackState)
                } else {
                    0L
                }
            }
        }

        return timerObservable
            .subscribe({ timeMillis ->
                // this livedata is initialised, encapsulated, and never has null values posted
                val currentEventList = podXEventMutableLiveData.value?.toMutableList()!!
                    .apply {
                        // assume no concurrent modification maybe wrongly
                        while(podXEventQueue.peek() != null
                            && podXEventQueue.peek()!!.timeStart < timeMillis) {
                            add(podXEventQueue.poll()!!)
                        }

                    }

                currentEventList.removeAll { it.timeEnd < timeMillis }

                podXEventMutableLiveData.postValue(currentEventList)
            }, { e: Throwable? -> Timber.e(e) }
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
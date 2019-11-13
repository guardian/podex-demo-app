package com.guardian.core.mediaplayer.podx

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.dao.PodXImageEventDao
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
    private val podXImageEventDao: PodXImageEventDao
) :
    PodXEventEmitter {

    // playback timer is started after we have gotten a valid podx event list from the repo so that
    // a concurrent modification exception can be avoided
    private var playbackTimerDisposable = Disposables.empty()

    private val podXEventMutableLiveData = MutableLiveData<List<PodXImageEvent>>()
        .apply {
            value = listOf()
        }

    override val podXImageEventLiveData: LiveData<List<PodXImageEvent>> = podXEventMutableLiveData

    // todo it doesn't make sense to use a PriQueue here
    private val podXImageEventQueue: PriorityQueue<PodXImageEvent> = PriorityQueue(30) { o1, o2 ->
            (o1.timeStart - o2.timeStart).toInt()
        }

    private var currentFeedDisposable = Disposables.empty()
    override fun registerCurrentFeedItem(feedItem: FeedItem) {
        currentFeedDisposable.dispose()
        podXEventMutableLiveData.postValue(listOf())
        currentFeedDisposable = podXImageEventDao.getPodXImageEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
            .subscribe({ feedPodXEventList ->
                playbackTimerDisposable.dispose()
                podXImageEventQueue.clear()
                podXImageEventQueue.addAll(feedPodXEventList)

                // trim events that have already been shown in case we are registering a feed item
                // that is already part way through playback.
                val currentState = mediaSessionConnection.playbackState.value
                if (currentState != null) {
                    val currentTime = getPlaybackPositionFromState(currentState)
                    while (podXImageEventQueue.peek()?.timeStart ?: -1 < currentTime) {
                        podXImageEventQueue.poll()
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
                        while(podXImageEventQueue.peek() != null
                            && podXImageEventQueue.peek()!!.timeStart < timeMillis) {
                            add(podXImageEventQueue.poll()!!)
                        }

                    }

                currentEventList.removeAll { it.timeEnd < timeMillis }

                podXEventMutableLiveData.postValue(currentEventList)
            }, { e: Throwable? ->
                Timber.e(e)
            })
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
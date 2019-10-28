package com.guardian.podxdemo.presentation.player

import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.id
import com.guardian.core.mediaplayer.extensions.isPlayEnabled
import com.guardian.core.mediaplayer.extensions.isPlaying
import com.guardian.core.mediaplayer.extensions.isPrepared
import com.guardian.core.podxevent.PodXEvent
import com.guardian.core.podxevent.PodXEventRepository
import com.guardian.podxdemo.R
import io.reactivex.Observable
import timber.log.Timber
import java.util.PriorityQueue
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class PlayerUiModel(
    val mediaMetadata: LiveData<MediaMetadataCompat>,
    val mediaButtonRes: LiveData<Int>,
    val mediaPlaybackPosition: LiveData<Long>
)

class PlayerViewModel
@Inject constructor(private val mediaSessionConnection: MediaSessionConnection,
                    private val podXEventRepository: PodXEventRepository) :
    ViewModel() {

    val playerUiModel by lazy {
        PlayerUiModel(mutableMediaMetadata,
            mutableMediaButtonRes,
            mutableMediaPlaybackPosition)
    }

    private val mutableMediaMetadata = mediaSessionConnection.nowPlaying
    private val mutableMediaPlaybackPosition = MutableLiveData<Long>().apply {
        mediaSessionConnection.playbackState.observeForever{
            this.postValue(
                it.position
            )
        }
    }
    private val mutableMediaButtonRes = MutableLiveData<Int>().apply {
        mediaSessionConnection.playbackState.observeForever {
            this.postValue(
                when (it.isPlaying) {
                    true -> R.drawable.baseline_pause_24
                    else -> R.drawable.baseline_play_arrow_24
                }
            )
        }
    }

    /**
     * Registers the current feed item uri with the mediaSessionConnection to begin playback
     */
    fun playFromUri(mediaUri: String) {
        val nowPlaying = mediaSessionConnection.nowPlaying.value
        val transportControls = mediaSessionConnection.transportControls

        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaUri == nowPlaying?.id) {
            mediaSessionConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Timber.w("%s%s", "Playable item clicked but neither play ",
                            "nor pause are enabled! (mediaId=$mediaUri)"
                        )
                    }
                }
            }
        } else {
            transportControls.playFromMediaId(mediaUri, null)
        }

        registerPlayerCallbacks()
    }


    //temporary local handling of playback time observable

    fun registerFeedItem(feedItem: FeedItem) {
        podXEventRepository.getEventsForFeedItem(
            feedItem
        ).subscribe{
            it.forEach {
                Timber.i("Adding podxevent ${it.urlString}")
            }
            podXQueue.clear()
            podXQueue.addAll(
                it
            )
        }
    }

    private val podXQueue = PriorityQueue<PodXEvent>(50) { o1: PodXEvent, o2: PodXEvent ->
        (o1.timeStart - o2.timeStart).toInt()
    }

    val podXeventMutableLiveData = MutableLiveData<PodXEvent>()

    /**
     * Internal function that recursively calls itself every [POSITION_UPDATE_INTERVAL_MILLIS] ms
     * to check the current playback position and updates the corresponding LiveData object when it
     * has changed.
     */
    private fun registerPlayerCallbacks() {
        val timerObservable = Observable.interval(
            100, TimeUnit.MILLISECONDS
        ).map {
            mediaSessionConnection.playbackState.value.let { playbackState ->
                if (playbackState != null) {
                    if (playbackState.state == PlaybackStateCompat.STATE_PLAYING) {
                        val timeDelta =
                            SystemClock.elapsedRealtime() - playbackState.lastPositionUpdateTime
                        (playbackState.position + (timeDelta * playbackState.playbackSpeed)).toLong()
                    } else {
                        playbackState.position
                    }
                } else {
                    0L
                }
            }
        }

        timerObservable.subscribe ({timeMillis ->
            if (podXQueue.peek() != null &&
                podXQueue.peek().timeStart < timeMillis) {
                podXeventMutableLiveData.postValue(podXQueue.poll())
            }
        },
            {t: Throwable? ->  Timber.e(t)}
        )
    }
}
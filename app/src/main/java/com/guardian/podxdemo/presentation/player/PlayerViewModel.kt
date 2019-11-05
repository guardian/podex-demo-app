package com.guardian.podxdemo.presentation.player

import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.id
import com.guardian.core.mediaplayer.extensions.isPlayEnabled
import com.guardian.core.mediaplayer.extensions.isPlaying
import com.guardian.core.mediaplayer.extensions.isPrepared
import com.guardian.core.mediaplayer.podx.PodXEventEmitter
import com.guardian.core.podxevent.PodXEvent
import com.guardian.podxdemo.R
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

data class PlayerUiModel(
    val mediaMetadataLiveData: LiveData<MediaMetadataCompat>,
    val mediaButtonRes: LiveData<Int>,
    val mediaPlaybackPositionLiveData: LiveData<Long>,
    val podXEventLiveData: LiveData<PodXEvent?>
)

class PlayerViewModel
@Inject constructor(
    private val mediaSessionConnection: MediaSessionConnection,
    private val podXEventEmitter: PodXEventEmitter
) :
    ViewModel() {

    val playerUiModel by lazy {
        PlayerUiModel(mediaMetadataMutableLiveData,
            mutableMediaButtonRes,
            mutableMediaPlaybackPosition,
            podXEventEmitter.podXEventLiveData)
    }

    private val compositeDisposable = CompositeDisposable()

    private val mediaMetadataMutableLiveData = MutableLiveData<MediaMetadataCompat>().apply {
        mediaSessionConnection.nowPlaying
            .observeForever { nowPlayingMetadata ->
                this.postValue(nowPlayingMetadata)
            }
    }

    private val mutableMediaPlaybackPosition = MutableLiveData<Long>().apply {
        mediaSessionConnection.playbackState.observeForever {
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
    }

    /**
     * changes the playback status without checking the current media uri
     */
    fun playpause() {
        val transportControls = mediaSessionConnection.transportControls

        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared) {
            mediaSessionConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Timber.w("%s%s", "Playable item clicked but neither play ",
                            "nor pause are enabled!"
                        )
                    }
                }
            }
        }
    }

    fun setFeedItem(feedItem: FeedItem) {
        podXEventEmitter.registerCurrentFeedItem(feedItem)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
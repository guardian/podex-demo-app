package com.guardian.podxdemo.presentation.player

import android.media.session.PlaybackState
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.currentPlayBackPosition
import com.guardian.core.mediaplayer.extensions.isPlayEnabled
import com.guardian.core.mediaplayer.extensions.isPlaying
import com.guardian.core.mediaplayer.extensions.isPrepared
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

data class PlayerUiModel(
    val mediaMetadataLiveData: LiveData<MediaMetadataCompat>,
    val mediaButtonIsPlaying: LiveData<Boolean>,
    val mediaPlaybackPositionLiveData: LiveData<Long>,
    val isPreparedLiveData: LiveData<Boolean>
)

class PlayerViewModel
@Inject constructor(
    private val mediaSessionConnection: MediaSessionConnection
) :
    ViewModel() {

    val playerUiModel by lazy {
        PlayerUiModel(mediaMetadataMutableLiveData,
            mutableMediaButtonIsPlaying,
            mutableMediaPlaybackPosition,
            mutableIsPreparedLiveData)
    }

    private val compositeDisposable = CompositeDisposable()
    private val playbackCheckHandler = Handler(Looper.getMainLooper())

    private val mediaMetadataMutableLiveData = MutableLiveData<MediaMetadataCompat>().apply {
        mediaSessionConnection.nowPlaying
            .observeForever { nowPlayingMetadata ->
                this.postValue(nowPlayingMetadata)
            }
    }

    private val mutableMediaPlaybackPosition = MutableLiveData<Long>().apply {
        mediaSessionConnection.playbackState.observeForever {

            Timber.i( "posting ${it.currentPlayBackPosition}")
            checkPlaybackPosition()

            if (it.playbackState == PlaybackState.STATE_PLAYING) {
                this.postValue(
                    it.currentPlayBackPosition
                )
            }

        }
    }
    private val mutableMediaButtonIsPlaying = MutableLiveData<Boolean>().apply {
        mediaSessionConnection.playbackState.observeForever { playbackState ->
            this.postValue(
                playbackState.isPlaying
            )
        }
    }

    private val mutableIsPreparedLiveData = MutableLiveData<Boolean>().apply {
        mediaSessionConnection.playbackState.observeForever { playbackState ->
            this.postValue(playbackState.isPrepared)
        }
    }

    /**
     * changes the playback status
     */
    fun playPause() {
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

    private fun checkPlaybackPosition(): Boolean = playbackCheckHandler.postDelayed({
        val currPosition = mediaSessionConnection.playbackState.value?.currentPlayBackPosition
        if (mutableMediaPlaybackPosition.value != currPosition && currPosition != null)
            mutableMediaPlaybackPosition.postValue(currPosition)
        if (mediaSessionConnection.playbackState.value?.isPlaying == true)
            checkPlaybackPosition()
    }, 250)

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }

    fun seekToPosition(time: Long) {
        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared) {
            mediaSessionConnection.transportControls.seekTo(time)
        }
    }
}
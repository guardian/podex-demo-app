package com.guardian.podxdemo.presentation.player

import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.isPlayEnabled
import com.guardian.core.mediaplayer.extensions.isPlaying
import com.guardian.core.mediaplayer.extensions.isPrepared
import com.guardian.podxdemo.R
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

data class PlayerUiModel(
    val mediaMetadataLiveData: LiveData<MediaMetadataCompat>,
    val mediaButtonRes: LiveData<Int>,
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
            mutableMediaButtonRes,
            mutableMediaPlaybackPosition,
            mutableIsPreparedLiveData)
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
        mediaSessionConnection.playbackState.observeForever { playbackState ->
            this.postValue(
                when (playbackState.isPlaying) {
                    true -> R.drawable.baseline_pause_24
                    else -> R.drawable.baseline_play_arrow_24
                }
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

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
package com.guardian.podx.presentation.podximage

import android.media.session.PlaybackState
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.currentPlayBackPosition
import com.guardian.core.mediaplayer.extensions.isPlaying
import com.guardian.core.mediaplayer.extensions.isPrepared
import javax.inject.Inject

data class PodXImageUiModel(
    val playbackTimeLiveData: LiveData<Long>
)

class PodXImageViewModel
@Inject constructor(
    private val mediaSessionConnection: MediaSessionConnection
) :
    ViewModel() {

    val podXImageUiModel by lazy {
        PodXImageUiModel(
            mutableMediaPlaybackPosition
        )
    }
    private val playbackCheckHandler = Handler(Looper.getMainLooper())

    fun skipToTimestamp(timeStamp: Long) {
        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared) {
            mediaSessionConnection.transportControls.seekTo(timeStamp)
        }
    }

    private val mutableMediaPlaybackPosition = MutableLiveData<Long>().apply {
        mediaSessionConnection.playbackState.observeForever {
            checkPlaybackPosition()

            if (it.playbackState == PlaybackState.STATE_PLAYING) {
                this.postValue(
                    it.currentPlayBackPosition
                )
            }
        }
    }

    private fun checkPlaybackPosition(): Boolean = playbackCheckHandler.postDelayed(
        {
            val currPosition = mediaSessionConnection.playbackState.value?.currentPlayBackPosition
            if (mutableMediaPlaybackPosition.value != currPosition && currPosition != null)
                mutableMediaPlaybackPosition.postValue(currPosition)
            if (mediaSessionConnection.playbackState.value?.isPlaying == true)
                checkPlaybackPosition()
        },
        250
    )
}

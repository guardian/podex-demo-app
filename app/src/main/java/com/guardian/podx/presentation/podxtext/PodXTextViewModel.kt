package com.guardian.podx.presentation.podxtext

import androidx.lifecycle.ViewModel
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.isPrepared
import javax.inject.Inject

class PodXTextViewModel
@Inject constructor(
    private val mediaSessionConnection: MediaSessionConnection
) :
    ViewModel() {

    fun skipToTimestamp(timeStamp: Long) {
        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared) {
            mediaSessionConnection.transportControls.seekTo(timeStamp)
        }
    }
}

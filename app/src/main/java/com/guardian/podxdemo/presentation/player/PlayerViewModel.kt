package com.guardian.podxdemo.presentation.player

import androidx.lifecycle.ViewModel
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.id
import com.guardian.core.mediaplayer.extensions.isPlayEnabled
import com.guardian.core.mediaplayer.extensions.isPlaying
import com.guardian.core.mediaplayer.extensions.isPrepared
import timber.log.Timber
import javax.inject.Inject

class PlayerViewModel
@Inject constructor(private val mediaSessionConnection: MediaSessionConnection) :
    ViewModel() {

    fun playFromUri(mediaUri: String) {
        // todo use subscription on feeds to populate a source so we have initialised
        //  transport controls here

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
}
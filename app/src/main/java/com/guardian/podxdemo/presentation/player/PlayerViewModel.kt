package com.guardian.podxdemo.presentation.player

import androidx.lifecycle.ViewModel
import com.guardian.mediaplayer.media.common.MediaSessionConnection
import javax.inject.Inject

class PlayerViewModel
@Inject constructor(val mediaSessionConnection: MediaSessionConnection)
    : ViewModel() {

}
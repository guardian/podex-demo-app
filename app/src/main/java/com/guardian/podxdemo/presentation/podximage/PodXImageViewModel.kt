package com.guardian.podxdemo.presentation.podximage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.mediaplayer.podx.PodXEventEmitter
import com.guardian.core.podxevent.PodXEvent
import javax.inject.Inject

data class PodXImageUiModel(
    val podXEventLiveData: LiveData<PodXEvent?>
)

class PodXImageViewModel
@Inject constructor(podXEventEmitter: PodXEventEmitter) :
    ViewModel() {
    val podXImageUiModel = PodXImageUiModel(podXEventEmitter.podXEventLiveData)
}
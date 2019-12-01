package com.guardian.podxdemo.presentation.podxeventscontainer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.mediaplayer.podx.PodXEventEmitter
import com.guardian.core.podxevent.PodXEvent
import javax.inject.Inject

data class PodXEventsContainerUiModel(
    val podXEventsListLiveData: LiveData<List<PodXEvent>>
)

class PodXEventsContainerViewModel
@Inject constructor(
    private val podXEventEmitter: PodXEventEmitter
) :
    ViewModel() {
    val podXEventsContainerUiModel by lazy {
        PodXEventsContainerUiModel(
            podXEventEmitter.podXEventLiveData
        )
    }
}
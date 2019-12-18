package com.guardian.podxdemo.presentation.podxeventscontainer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.mediaplayer.podx.PodXEventEmitter
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXWebEvent
import javax.inject.Inject

data class PodXEventsContainerUiModel(
    val podXImageEventsListLiveData: LiveData<List<PodXImageEvent>>,
    val podXWebEventsListLiveData: LiveData<List<PodXWebEvent>>
)

class PodXEventsContainerViewModel
@Inject constructor(
    private val podXEventEmitter: PodXEventEmitter
) :
    ViewModel() {
    val podXEventsContainerUiModel by lazy {
        PodXEventsContainerUiModel(
            podXEventEmitter.podXImageEventLiveData,
            podXEventEmitter.podXWebEventLiveData
        )
    }
}
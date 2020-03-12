package com.guardian.podxdemo.presentation.podxeventscontainer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.mediaplayer.podx.PodXEventEmitter
import com.guardian.core.podxevent.PodXCallPromptEvent
import com.guardian.core.podxevent.PodXFeedBackEvent
import com.guardian.core.podxevent.PodXFeedLinkEvent
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXNewsLetterSignUpEvent
import com.guardian.core.podxevent.PodXPollEvent
import com.guardian.core.podxevent.PodXSocialPromptEvent
import com.guardian.core.podxevent.PodXSupportEvent
import com.guardian.core.podxevent.PodXTextEvent
import com.guardian.core.podxevent.PodXWebEvent
import javax.inject.Inject

data class PodXEventsContainerUiModel(
    val podXImageEventsListLiveData: LiveData<List<PodXImageEvent>>,
    val podXWebEventsListLiveData: LiveData<List<PodXWebEvent>>,
    val podXSupportEventsListLiveData: LiveData<List<PodXSupportEvent>>,
    val podXCallPromptEventsListLiveData: LiveData<List<PodXCallPromptEvent>>,
    val podXFeedBackEventsListLiveData: LiveData<List<PodXFeedBackEvent>>,
    val podXFeedLinkEventsListLiveData: LiveData<List<PodXFeedLinkEvent>>,
    val podXNewsLetterSignUpEventsListLiveData: LiveData<List<PodXNewsLetterSignUpEvent>>,
    val podXPollEventsListLiveData: LiveData<List<PodXPollEvent>>,
    val podXSocialPromptEventsListLiveData: LiveData<List<PodXSocialPromptEvent>>,
    val podXTextEventsListLiveData: LiveData<List<PodXTextEvent>>
)

class PodXEventsContainerViewModel
@Inject constructor(
    private val podXEventEmitter: PodXEventEmitter
) :
    ViewModel() {
    val podXEventsContainerUiModel by lazy {
        PodXEventsContainerUiModel(
            podXEventEmitter.podXImageEventLiveData,
            podXEventEmitter.podXWebEventLiveData,
            podXEventEmitter.podXSupportEventLiveData,
            podXEventEmitter.podXCallPromptEventLiveData,
            podXEventEmitter.podXFeedBackEventLiveData,
            podXEventEmitter.podXFeedLinkEventLiveData,
            podXEventEmitter.podXNewsLetterSignUpEventLiveData,
            podXEventEmitter.podXPollEventLiveData,
            podXEventEmitter.podXSocialPromptEventLiveData,
            podXEventEmitter.podXTextEventLiveData
        )
    }
}
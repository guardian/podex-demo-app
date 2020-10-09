package com.guardian.podx.presentation.playersummaryimage

import androidx.lifecycle.LiveData
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

data class PlayerSummaryImageUiModel(
    val podXImageEventsListLiveData: LiveData<List<PodXImageEvent>>,
    val podXWebEventsListLiveData: LiveData<List<PodXWebEvent>>,
    val podXSupportEventsListLiveData: LiveData<List<PodXSupportEvent>>,
    val podXCallPromptEventsListLiveData: LiveData<List<PodXCallPromptEvent>>,
    val podXFeedBackEventsListLiveData: LiveData<List<PodXFeedBackEvent>>,
    val podXFeedLinkEventsListLiveData: LiveData<List<PodXFeedLinkEvent>>,
    val podXNewsLetterSignUpEventsListLiveData: LiveData<List<PodXNewsLetterSignUpEvent>>,
    val podXPollEventsListLiveData: LiveData<List<PodXPollEvent>>,
    val podXSocialPromptEventsListLiveData: LiveData<List<PodXSocialPromptEvent>>,
    val podXTextEventsListLiveData: LiveData<List<PodXTextEvent>>,
    val hasEvents: LiveData<Boolean>,
    val mediaImageUrl: LiveData<String>
)

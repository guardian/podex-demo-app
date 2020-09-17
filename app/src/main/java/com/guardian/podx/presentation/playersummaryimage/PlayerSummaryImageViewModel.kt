package com.guardian.podx.presentation.playersummaryimage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.albumArtUri
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

class PlayerSummaryImageViewModel
@Inject constructor(
    private val podXEventEmitter: PodXEventEmitter,
    mediaSessionConnection: MediaSessionConnection
) :
    ViewModel() {

    private val hasEventsLiveData = MutableLiveData(false).apply {
        podXEventEmitter.podXImageEventLiveData.observeForever {
            this.postValue(checkAllEvents())
        }

        podXEventEmitter.podXTextEventLiveData.observeForever {
            this.postValue(checkAllEvents())
        }

        podXEventEmitter.podXSocialPromptEventLiveData.observeForever {
            this.postValue(checkAllEvents())
        }

        podXEventEmitter.podXPollEventLiveData.observeForever {
            this.postValue(checkAllEvents())
        }

        podXEventEmitter.podXWebEventLiveData.observeForever {
            this.postValue(checkAllEvents())
        }

        podXEventEmitter.podXNewsLetterSignUpEventLiveData.observeForever {
            this.postValue(checkAllEvents())
        }

        podXEventEmitter.podXFeedLinkEventLiveData.observeForever {
            this.postValue(checkAllEvents())
        }

        podXEventEmitter.podXFeedBackEventLiveData.observeForever {
            this.postValue(checkAllEvents())
        }

        podXEventEmitter.podXCallPromptEventLiveData.observeForever {
            this.postValue(checkAllEvents())
        }
    }

    private val mediaImageUrlLiveData = MutableLiveData<String>().apply {
        this.postValue(
            mediaSessionConnection.nowPlaying
                .value
                ?.albumArtUri.toString()
        )
        mediaSessionConnection.nowPlaying.observeForever {
            this.postValue(it.albumArtUri.toString())
        }
    }

    val playerSummaryImageUiModel by lazy {
        PlayerSummaryImageUiModel(
            podXEventEmitter.podXImageEventLiveData,
            podXEventEmitter.podXWebEventLiveData,
            podXEventEmitter.podXSupportEventLiveData,
            podXEventEmitter.podXCallPromptEventLiveData,
            podXEventEmitter.podXFeedBackEventLiveData,
            podXEventEmitter.podXFeedLinkEventLiveData,
            podXEventEmitter.podXNewsLetterSignUpEventLiveData,
            podXEventEmitter.podXPollEventLiveData,
            podXEventEmitter.podXSocialPromptEventLiveData,
            podXEventEmitter.podXTextEventLiveData,
            hasEventsLiveData,
            mediaImageUrlLiveData
        )
    }

    private fun checkAllEvents(): Boolean =
        podXEventEmitter.podXImageEventLiveData.value?.isNotEmpty() == true ||
            podXEventEmitter.podXWebEventLiveData.value?.isNotEmpty() == true ||
            podXEventEmitter.podXSupportEventLiveData.value?.isNotEmpty() == true ||
            podXEventEmitter.podXCallPromptEventLiveData.value?.isNotEmpty() == true ||
            podXEventEmitter.podXFeedBackEventLiveData.value?.isNotEmpty() == true ||
            podXEventEmitter.podXFeedLinkEventLiveData.value?.isNotEmpty() == true ||
            podXEventEmitter.podXNewsLetterSignUpEventLiveData.value?.isNotEmpty() == true ||
            podXEventEmitter.podXPollEventLiveData.value?.isNotEmpty() == true ||
            podXEventEmitter.podXSocialPromptEventLiveData.value?.isNotEmpty() == true ||
            podXEventEmitter.podXTextEventLiveData.value?.isNotEmpty() == true
}

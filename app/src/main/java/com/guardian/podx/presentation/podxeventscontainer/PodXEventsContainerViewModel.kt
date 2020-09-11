package com.guardian.podx.presentation.podxeventscontainer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.feed.FeedRepository
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.FeedItemRepository
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.id
import com.guardian.core.mediaplayer.extensions.isPrepared
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
import io.reactivex.Flowable
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
    val podXTextEventsListLiveData: LiveData<List<PodXTextEvent>>,
    val hasEvents: Boolean,
    val episodePodXImageEventsListLiveData: LiveData<List<PodXImageEvent>>,
    val episodePodXWebEventsListLiveData: LiveData<List<PodXWebEvent>>,
    val episodePodXSupportEventsListLiveData: LiveData<List<PodXSupportEvent>>,
    val episodePodXCallPromptEventsListLiveData: LiveData<List<PodXCallPromptEvent>>,
    val episodePodXFeedBackEventsListLiveData: LiveData<List<PodXFeedBackEvent>>,
    val episodePodXFeedLinkEventsListLiveData: LiveData<List<PodXFeedLinkEvent>>,
    val episodePodXNewsLetterSignUpEventsListLiveData: LiveData<List<PodXNewsLetterSignUpEvent>>,
    val episodePodXPollEventsListLiveData: LiveData<List<PodXPollEvent>>,
    val episodePodXSocialPromptEventsListLiveData: LiveData<List<PodXSocialPromptEvent>>,
    val episodePodXTextEventsListLiveData: LiveData<List<PodXTextEvent>>
)

class PodXEventsContainerViewModel
@Inject constructor(
    private val podXEventEmitter: PodXEventEmitter,
    private val podXFeedRepository: FeedRepository,
    private val podXFeedItemRepository: FeedItemRepository,
    private val mediaSessionConnection: MediaSessionConnection
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
            podXEventEmitter.podXTextEventLiveData,
            checkAllEvents(),
            podXEventEmitter.podXPendingImageEventLiveData,
            podXEventEmitter.podXPendingWebEventLiveData,
            podXEventEmitter.podXPendingSupportEventLiveData,
            podXEventEmitter.podXCallPromptEventLiveData,
            podXEventEmitter.podXFeedBackEventLiveData,
            podXEventEmitter.podXFeedLinkEventLiveData,
            podXEventEmitter.podXNewsLetterSignUpEventLiveData,
            podXEventEmitter.podXPendingPollEventLiveData,
            podXEventEmitter.podXPendingSocialPromptEventLiveData,
            podXEventEmitter.podXPendingTextEventLiveData
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

    fun openGetFeedItemFromFeedLink(feedLinkEvent: PodXFeedLinkEvent): Flowable<FeedItem> {
        return podXFeedRepository.getFeedWithoutUpDate(feedLinkEvent.remoteFeedUrlString)
            .toFlowable()
            .flatMap { _ ->
                podXFeedItemRepository.getFeedItemForSearchParams(
                    feedLinkEvent.remoteFeedItemTitle,
                    feedLinkEvent.remoteFeedItemPubDate,
                    feedLinkEvent.remoteFeedItemGuid,
                    feedLinkEvent.remoteFeedItemUrlString
                ).map {
                    it.first()
                }
            }
    }

    fun prepareFeedItemForPlayback(feedItem: FeedItem) {
        val nowPlaying = mediaSessionConnection.nowPlaying.value
        val transportControls = mediaSessionConnection.transportControls

        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (!(isPrepared && feedItem.feedItemAudioUrl == nowPlaying?.id)) {
            transportControls.prepareFromMediaId(feedItem.feedItemAudioUrl, null)
            podXEventEmitter.registerCurrentFeedItem(feedItem)
        }
    }

    fun skipToTimestamp(timeStamp: Long) {
        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared) {
            mediaSessionConnection.transportControls.seekTo(timeStamp)
        }
    }
}

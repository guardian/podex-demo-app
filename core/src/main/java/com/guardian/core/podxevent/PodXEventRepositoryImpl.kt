package com.guardian.core.podxevent

import com.guardian.core.feeditem.FeedItem
import com.guardian.core.podxevent.dao.PodXImageEventDao
import com.guardian.core.podxevent.dao.PodXSupportEventDao
import com.guardian.core.podxevent.dao.PodXWebEventDao
import io.reactivex.Flowable
import javax.inject.Inject

class PodXEventRepositoryImpl
    @Inject constructor(
        private val podXImageEventDao: PodXImageEventDao,
        private val podXWebEventDao: PodXWebEventDao,
        private val podXSupportEventDao: PodXSupportEventDao
    ) :
    PodXEventRepository {
    override fun deletePodXEventsForFeedItem(feedItem: FeedItem) {
        podXImageEventDao.removePodXImageEventList(feedItem.feedItemAudioUrl)
        podXWebEventDao.removePodXWebEventList(feedItem.feedItemAudioUrl)
    }

    override fun addPodXImageEvents(podXImageEvents: List<PodXImageEvent>) {
        podXImageEventDao.putPodXImageEventList(podXImageEvents)
    }

    override fun getImageEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXImageEvent>> {
        return podXImageEventDao.getPodXImageEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun addPodXWebEvents(podXWebEvents: List<PodXWebEvent>) {
        podXWebEventDao.putPodXWebEventList(podXWebEvents)
    }

    override fun addPodXSupportEvents(podXSupportEvents: List<PodXSupportEvent>) {
        podXSupportEventDao.putPodXSupportEventList(podXSupportEvents)
    }

    override fun addPodXCallPromptEvents(PodXCallPromptEvents: List<PodXCallPromptEvent>) {
        TODO("Not yet implemented")
    }

    override fun addPodXFeedBackEvents(PodXFeedBackEvents: List<PodXFeedBackEvent>) {
        TODO("Not yet implemented")
    }

    override fun addPodXFeedLinkEvents(PodXFeedLinkEvents: List<PodXFeedLinkEvent>) {
        TODO("Not yet implemented")
    }

    override fun addPodXNewsLetterSignUpEvents(PodXNewsLetterSignUpEvents: List<PodXNewsLetterSignUpEvent>) {
        TODO("Not yet implemented")
    }

    override fun addPodXPollEvents(PodXPollEvents: List<PodXPollEvent>) {
        TODO("Not yet implemented")
    }

    override fun addPodXSocialPromptEvents(PodXSocialPromptEvents: List<PodXSocialPromptEvent>) {
        TODO("Not yet implemented")
    }

    override fun addPodXTextEvents(PodXTextEvents: List<PodXTextEvent>) {
        TODO("Not yet implemented")
    }

    override fun getWebEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXWebEvent>> {
        return podXWebEventDao.getPodXWebEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun getSupportEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXSupportEvent>> {
        return podXSupportEventDao.getPodXSupportEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun getCallPromptEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXCallPromptEvent>> {
        TODO("Not yet implemented")
    }

    override fun getFeedBackEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXFeedBackEvent>> {
        TODO("Not yet implemented")
    }

    override fun getFeedLinkEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXFeedLinkEvent>> {
        TODO("Not yet implemented")
    }

    override fun getNewsLetterSignUpEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXNewsLetterSignUpEvent>> {
        TODO("Not yet implemented")
    }

    override fun getPollEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXPollEvent>> {
        TODO("Not yet implemented")
    }

    override fun getSocialPromptEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXSocialPromptEvent>> {
        TODO("Not yet implemented")
    }

    override fun getTextEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXTextEvent>> {
        TODO("Not yet implemented")
    }
}
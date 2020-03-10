package com.guardian.core.podxevent

import com.guardian.core.feeditem.FeedItem
import com.guardian.core.podxevent.dao.PodXCallPromptEventDao
import com.guardian.core.podxevent.dao.PodXFeedBackEventDao
import com.guardian.core.podxevent.dao.PodXFeedLinkEventDao
import com.guardian.core.podxevent.dao.PodXImageEventDao
import com.guardian.core.podxevent.dao.PodXNewsLetterSignUpEventDao
import com.guardian.core.podxevent.dao.PodXPollEventDao
import com.guardian.core.podxevent.dao.PodXSocialPromptEventDao
import com.guardian.core.podxevent.dao.PodXSupportEventDao
import com.guardian.core.podxevent.dao.PodXTextEventDao
import com.guardian.core.podxevent.dao.PodXWebEventDao
import io.reactivex.Flowable
import javax.inject.Inject

class PodXEventRepositoryImpl
    @Inject constructor(
        private val podXImageEventDao: PodXImageEventDao,
        private val podXWebEventDao: PodXWebEventDao,
        private val podXSupportEventDao: PodXSupportEventDao,
        private val podXCallPromptEventDao: PodXCallPromptEventDao,
        private val podXFeedBackEventDao: PodXFeedBackEventDao,
        private val podXFeedLinkEventDao: PodXFeedLinkEventDao,
        private val podXNewsLetterSignUpEventDao: PodXNewsLetterSignUpEventDao,
        private val podXPollEventDao: PodXPollEventDao,
        private val podXSocialPromptEventDao: PodXSocialPromptEventDao,
        private val podXTextEventDao: PodXTextEventDao
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

    override fun addPodXCallPromptEvents(podXCallPromptEvents: List<PodXCallPromptEvent>) {
        podXCallPromptEventDao.putPodXCallPromptEventList(podXCallPromptEvents)
    }

    override fun addPodXFeedBackEvents(podXFeedBackEvents: List<PodXFeedBackEvent>) {
        podXFeedBackEventDao.putPodXFeedBackEventList(podXFeedBackEvents)
    }

    override fun addPodXFeedLinkEvents(podXFeedLinkEvents: List<PodXFeedLinkEvent>) {
        podXFeedLinkEventDao.putPodXFeedLinkEventList(podXFeedLinkEvents)
    }

    override fun addPodXNewsLetterSignUpEvents(podXNewsLetterSignUpEvents: List<PodXNewsLetterSignUpEvent>) {
        podXNewsLetterSignUpEventDao.putPodXNewsLetterSignUpEventList(podXNewsLetterSignUpEvents)
    }

    override fun addPodXPollEvents(podXPollEvents: List<PodXPollEvent>) {
        podXPollEventDao.putPodXPollEventList(podXPollEvents)
    }

    override fun addPodXSocialPromptEvents(podXSocialPromptEvents: List<PodXSocialPromptEvent>) {
        podXSocialPromptEventDao.putPodXSocialPromptEventList(podXSocialPromptEvents)
    }

    override fun addPodXTextEvents(podXTextEvents: List<PodXTextEvent>) {
        podXTextEventDao.putPodXTextEventList(podXTextEvents)
    }

    override fun getWebEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXWebEvent>> {
        return podXWebEventDao.getPodXWebEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun getSupportEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXSupportEvent>> {
        return podXSupportEventDao.getPodXSupportEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun getCallPromptEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXCallPromptEvent>> {
        return podXCallPromptEventDao.getPodXCallPromptEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun getFeedBackEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXFeedBackEvent>> {
        return podXFeedBackEventDao.getPodXFeedBackEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun getFeedLinkEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXFeedLinkEvent>> {
        return podXFeedLinkEventDao.getPodXFeedLinkEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun getNewsLetterSignUpEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXNewsLetterSignUpEvent>> {
        return podXNewsLetterSignUpEventDao.getPodXNewsLetterSignUpEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun getPollEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXPollEvent>> {
        return podXPollEventDao.getPodXPollEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun getSocialPromptEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXSocialPromptEvent>> {
        return podXSocialPromptEventDao.getPodXSocialPromptEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun getTextEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXTextEvent>> {
        return podXTextEventDao.getPodXTextEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }
}
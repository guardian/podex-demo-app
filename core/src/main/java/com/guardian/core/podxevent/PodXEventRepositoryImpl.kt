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

    override fun getWebEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXWebEvent>> {
        return podXWebEventDao.getPodXWebEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }

    override fun getSupportEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXSupportEvent>> {
        return podXSupportEventDao.getPodXSupportEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }
}
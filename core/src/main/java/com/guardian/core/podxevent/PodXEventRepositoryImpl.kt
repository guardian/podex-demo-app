package com.guardian.core.podxevent

import com.guardian.core.feeditem.FeedItem
import com.guardian.core.podxevent.dao.PodXImageEventDao
import io.reactivex.Flowable
import javax.inject.Inject

class PodXEventRepositoryImpl
    @Inject constructor(
        private val podXImageEventDao: PodXImageEventDao
    ) :
    PodXEventRepository {
    override fun getWebEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXWebEvent>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePodXEventsForFeedItem(feedItem: FeedItem) {
        podXImageEventDao.removePodXImageEventList(feedItem.feedItemAudioUrl)
    }

    override fun addPodXEvents(podXImageEvents: List<PodXImageEvent>) {
        podXImageEventDao.putPodXImageEventList(podXImageEvents)
    }

    override fun getImageEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXImageEvent>> {
        return podXImageEventDao.getPodXImageEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }
}
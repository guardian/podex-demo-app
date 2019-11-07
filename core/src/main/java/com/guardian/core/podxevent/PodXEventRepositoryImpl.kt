package com.guardian.core.podxevent

import com.guardian.core.feeditem.FeedItem
import com.guardian.core.podxevent.dao.PodXEventDao
import io.reactivex.Flowable
import javax.inject.Inject

class PodXEventRepositoryImpl
    @Inject constructor(
        private val podXEventDao: PodXEventDao
    ) :
    PodXEventRepository {
    override fun deletePodXEventsForFeedItem(feedItem: FeedItem) {
        podXEventDao.removePodXEventList(feedItem.feedItemAudioUrl)
    }

    override fun addPodXEvents(podXEvents: List<PodXEvent>) {
        podXEventDao.putPodxEventList(podXEvents)
    }

    override fun getEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXEvent>> {
        return podXEventDao.getPodXEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }
}
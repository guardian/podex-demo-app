package com.guardian.core.feeditem

import com.guardian.core.feed.Feed
import com.guardian.core.feeditem.dao.FeedItemDao
import io.reactivex.Flowable
import javax.inject.Inject

class FeedItemRepositoryImpl @Inject constructor(
    private val feedItemDao: FeedItemDao
)
    : FeedItemRepository {

    override fun getFeedItemsForFeed(feed: Feed): Flowable<List<FeedItem>> {
        //todo fire off feed api update

        return feedItemDao.getFeedItemsForFeedUrl(feed.feedUrlString)
    }

    override fun getFeedItemForUrlString(feedItemUrlString: String): Flowable<FeedItem> {
        //todo fire off feed api update

        return feedItemDao.getFeedItemForUrlString(feedItemUrlString)
    }
}
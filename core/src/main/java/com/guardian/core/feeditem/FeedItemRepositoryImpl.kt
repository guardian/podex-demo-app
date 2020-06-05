package com.guardian.core.feeditem

import com.guardian.core.feed.Feed
import com.guardian.core.feeditem.dao.FeedItemDao
import io.reactivex.Flowable
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

class FeedItemRepositoryImpl @Inject constructor(
    private val feedItemDao: FeedItemDao
)
    : FeedItemRepository {
    override fun addFeedItems(feedItems: List<FeedItem>) {
        feedItemDao.addFeedList(feedItems)
    }

    override fun getFeedItemsForFeed(feed: Feed): Flowable<List<FeedItem>> {
        return feedItemDao.getFeedItemsForFeedUrl(feed.feedUrlString)
    }

    override fun getFeedItemForUrlString(feedItemUrlString: String): Flowable<FeedItem> {
        return feedItemDao.getFeedItemForUrlString(feedItemUrlString)
    }

    override fun getFeedItemForSearchParams(
        feedItemTitle: String?,
        feedItemPubDate: Date?,
        feedItemGuid: String?,
        feedItemUrlString: String?
    ): Flowable<List<FeedItem>> {
        return feedItemDao.getFeedItemsWithLinkParams(feedItemTitle, feedItemGuid,
            feedItemUrlString, feedItemPubDate)
    }
}
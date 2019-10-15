package com.guardian.core.feeditem

import androidx.lifecycle.LiveData
import com.guardian.core.feed.Feed
import com.guardian.core.feed.FeedRepository
import com.guardian.core.feeditem.dao.FeedItemDao
import javax.inject.Inject

class FeedItemRepositoryImpl @Inject constructor(
    private val feedItemDao: FeedItemDao,
    private val feedRepository: FeedRepository)
    : FeedItemRepository {

    override suspend fun getFeedItemsForFeed(feed: Feed): LiveData<List<FeedItem>> {
        return feedItemDao.getFeedItemsForFeedUrl(feed.feedUrlString)
    }

    override suspend fun getFeedItemForUrlString(feedItemUrlString: String): LiveData<FeedItem> {
        return feedItemDao.getFeedItemForUrlString(feedItemUrlString)
    }
}
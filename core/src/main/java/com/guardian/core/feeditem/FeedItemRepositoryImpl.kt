package com.guardian.core.feeditem

import androidx.lifecycle.LiveData
import com.guardian.core.feed.Feed
import com.guardian.core.feeditem.dao.FeedItemDao
import javax.inject.Inject

class FeedItemRepositoryImpl @Inject constructor(val feedItemDao: FeedItemDao) : FeedItemRepository {
    override fun getFeedItemsForFeed(feed: Feed): LiveData<List<FeedItem>> {

    }

    override fun getFeedItemForUrlString(feedUrlString: String): LiveData<FeedItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
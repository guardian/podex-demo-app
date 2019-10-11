package com.guardian.core.feeditem

import androidx.lifecycle.LiveData
import com.guardian.core.feed.Feed

interface FeedItemRepository {
    suspend fun getFeedItemsForFeed(feed: Feed): LiveData<List<FeedItem>>
    suspend fun getFeedItemForUrlString(feedItemUrlString: String): LiveData<FeedItem>
}
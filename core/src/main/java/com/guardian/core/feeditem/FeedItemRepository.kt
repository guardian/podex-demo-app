package com.guardian.core.feeditem

import androidx.lifecycle.LiveData
import com.guardian.core.feed.Feed

interface FeedItemRepository {
    fun getFeedItemsForFeed(feed: Feed): LiveData<List<FeedItem>>
    fun getFeedItemForUrlString(feedUrlString: String): LiveData<FeedItem>
}
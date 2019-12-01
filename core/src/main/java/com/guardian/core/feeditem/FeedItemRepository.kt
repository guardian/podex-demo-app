package com.guardian.core.feeditem

import com.guardian.core.feed.Feed
import io.reactivex.Flowable

interface FeedItemRepository {
    /**
     * Retrieve items associated with a feed
     *
     * @param feed a feed with a valid url string set
     * @return a [Flowable] that emits the most current list of feed items associated with [feed]
     */
    fun getFeedItemsForFeed(feed: Feed): Flowable<List<FeedItem>>

    /**
     * Return a [FeedItem] for a given audio url
     *
     * @param feedItemUrlString the url from the feed items enclosure tag
     * @return a [Flowable] that emits the most current version of the [FeedItem] and updates
     */
    fun getFeedItemForUrlString(feedItemUrlString: String): Flowable<FeedItem>

    /**
     * Add list of [FeedItem]s to the repository
     *
     * @param feedItems
     */
    fun addFeedItems(feedItems: List<FeedItem>)
}
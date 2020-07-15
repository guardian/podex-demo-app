package com.guardian.core.feeditem

import com.guardian.core.feed.Feed
import io.reactivex.Flowable
import java.util.Date

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
     * Given a set of arguments returns a list of matching feed items from the repo, ideally one
     * item matches given parameters
     *
     * @param feedItemTitle the title of the episode, corresponding to the title field of an item
     * in the RSS feed
     * @param feedItemPubDate the [Date] that can be converted from the pubDate field of the RSS
     * feed
     * @param feedItemGuid a guid is not necessarily unique in the RSS spec, and can take any form
     * @param feedItemUrlString the enclusure url for a given feed item
     * @return a [Flowable] that emits the most current version of the [FeedItem] and updates
     */
    fun getFeedItemForSearchParams(feedItemTitle: String?,
                                   feedItemPubDate: Date?,
                                   feedItemGuid: String?,
                                   feedItemUrlString: String?): Flowable<List<FeedItem>>

    /**
     * Add list of [FeedItem]s to the repository
     *
     * @param feedItems
     */
    fun addFeedItems(feedItems: List<FeedItem>)
}
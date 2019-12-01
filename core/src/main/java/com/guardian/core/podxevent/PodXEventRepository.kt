package com.guardian.core.podxevent

import com.guardian.core.feeditem.FeedItem
import io.reactivex.Flowable

/**
 * A Repository for accessing PodXEvents
 *
 * Currently all PodX Events are taken from the feed rss, and are associated with a given feed item.
 */
interface PodXEventRepository {
    /**
     * Returns a [Flowable] that emits a list of all [PodXEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXEvent]s and any updates to that list
     */
    fun getEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXEvent>>

    /**
     * Clear all [PodXEvent]s associated with a [FeedItem]
     *
     * @param feedItem feed item with to be associated with [PodXEvent] by it's
     * [FeedItem.feedUrlString]
     */
    fun deletePodXEventsForFeedItem(feedItem: FeedItem)

    /**
     * Add a list of [PodXEvent]s to the repository
     *
     * @param podXEvents the list of events to be added
     */
    fun addPodXEvents(podXEvents: List<PodXEvent>)
}
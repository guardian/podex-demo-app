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
     * Returns a [Flowable] that emits a list of all [PodXImageEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXImageEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXImageEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXImageEvent]s and any updates to that list
     */
    fun getImageEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXImageEvent>>

    /**
     * Returns a [Flowable] that emits a list of all [PodXWebEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXWebEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXWebEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXWebEvent]s and any updates to that list
     */
    fun getWebEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXWebEvent>>

    /**
     * Returns a [Flowable] that emits a list of all [PodXSupportEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXSupportEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXWebEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXSupportEvent]s and any updates to that list
     */
    fun getSupportEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXSupportEvent>>

    /**
     * Clear all [PodXImageEvent]s associated with a [FeedItem]
     *
     * @param feedItem feed item with to be associated with [PodXImageEvent] by it's
     * [FeedItem.feedUrlString]
     */
    fun deletePodXEventsForFeedItem(feedItem: FeedItem)

    /**
     * Add a list of [PodXImageEvent]s to the repository
     *
     * @param podXImageEvents the list of events to be added
     */
    fun addPodXImageEvents(podXImageEvents: List<PodXImageEvent>)

    /**
     * Add a list of [PodXWebEvent]s to the repository
     *
     * @param podXWebEvents the list of events to be added
     */
    fun addPodXWebEvents(podXWebEvents: List<PodXWebEvent>)

    /**
     * Add a list of [PodXSupportEvent]s to the repository
     *
     * @param podXWebEvents the list of events to be added
     */
    fun addPodXSupportEvents(podXSupportEvents: List<PodXSupportEvent>)
}
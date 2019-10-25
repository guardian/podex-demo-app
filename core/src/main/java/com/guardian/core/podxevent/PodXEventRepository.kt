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
     */
    fun getEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXEvent>>
}
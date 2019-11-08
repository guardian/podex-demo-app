package com.guardian.core.feed

import io.reactivex.Flowable

interface FeedRepository {
    /**
     * Retrieves the [Feed] for a given feed url
     *
     * @param feedUrl the link to an rss feed
     * @return a [Flowable] that emits the most current version of the [Feed] and re-emits as it
     *      updates.
     */
    fun getFeed(feedUrl: String): Flowable<Feed>

    /**
     * Retrieves all stored [Feed] objects
     *
     * @return a [Flowable] that emits a list of all [Feed] objects and re emits when a feed is
     *      added, removed, or updated
     */
    fun getFeeds(): Flowable<List<Feed>>
}
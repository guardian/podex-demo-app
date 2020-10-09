package com.guardian.core.mediametadata

import android.support.v4.media.MediaMetadataCompat
import io.reactivex.Flowable

/**
 * A repository for retrieving Feed and Feed Item data mapped to the MediaMetadataCompat object to
 * be consumed by the MediaBrowserServiceCompat
 */
interface MediaMetadataRepository {
    /**
     * Retrieve a [MediaMetadataCompat] made for an item in an xml feed. If no item has been cached
     * an error will be emitted
     *
     * @param idString the enclosure url of a feed item
     * @return a [Flowable] with a [MediaMetadataCompat] that  contains metadata for an item within
     * a feed
     */
    fun getMetadataForId(idString: String): Flowable<MediaMetadataCompat>

    /**
     * Retrieve a [MediaMetadataCompat] for every xml feed item that has been stored in the local
     * repo. If no item has been cached an error will be emitted
     *
     * @return a [Flowable] with a list of [MediaMetadataCompat] for every stored FeedItem
     */
    fun getStoredMetadata(): Flowable<List<MediaMetadataCompat>>
}

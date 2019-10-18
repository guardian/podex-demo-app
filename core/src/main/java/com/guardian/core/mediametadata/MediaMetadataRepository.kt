package com.guardian.core.mediametadata

import android.support.v4.media.MediaMetadataCompat
import io.reactivex.rxjava3.core.Flowable

interface MediaMetadataRepository {
    /**
     * Retrieve a [MediaMetaDataCompat] made for an item in an xml feed. If no item has been cached
     * an error will be emitted
     *
     * @param idString the enclosure url of a feed item
     * @return a [Flowable] with a [MediaMetadataCompat] that  contains metadata for an item within
     * a feed
     */
    fun getMetadataForId(idString: String): Flowable<MediaMetadataCompat>
}
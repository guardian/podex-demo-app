package com.guardian.core.mediaplayer.ShadowMediaBrowserCompat

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.EXTRA_PAGE
import android.support.v4.media.MediaBrowserCompat.EXTRA_PAGE_SIZE
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.annotation.RealObject
import org.robolectric.shadow.api.Shadow
import org.robolectric.util.ReflectionHelpers.ClassParameter
import java.util.ArrayList
import java.util.LinkedHashMap

/**
 * This will mimic the connection to a [MediaBrowserServiceCompat] by creating and maintaining
 * its own account of [MediaItem]s.
 */
@Implements(MediaBrowserCompat::class)
class ShadowMediaBrowserCompat {

    private val handler = Handler()
    @RealObject
    private val mediaBrowser: MediaBrowserCompat? = null

    private val mediaItems = LinkedHashMap<String, MediaItem>()
    private val mediaItemChildren = LinkedHashMap<MediaItem, List<MediaItem>>()

    @get:Implementation
    protected var isConnected: Boolean = false
        private set
    private var connectionCallback: MediaBrowserCompat.ConnectionCallback? = null
    private var rootId = "root_id"

    protected val root: String
        @Implementation
        get() {
            check(isConnected) { "Can't call getRoot() while not connected." }
            return rootId
        }

    /** @return a copy of the internal [Map] that maps [MediaItem]s to their children.
     */
    val copyOfMediaItemChildren: Map<MediaItem, List<MediaItem>>
        get() {
            val copyOfMediaItemChildren = LinkedHashMap<MediaItem, List<MediaItem>>()
            for (parent in mediaItemChildren.keys) {
                val children = ArrayList(mediaItemChildren[parent]!!)
                copyOfMediaItemChildren[parent] = children
            }
            return copyOfMediaItemChildren
        }

    @Implementation
    protected fun __constructor__(
        context: Context,
        serviceComponent: ComponentName,
        callback: MediaBrowserCompat.ConnectionCallback,
        rootHints: Bundle?
    ) {
        connectionCallback = callback
        Shadow.invokeConstructor<MediaBrowserCompat>(
            MediaBrowserCompat::class.java,
            mediaBrowser,
            ClassParameter.from(Context::class.java, context),
            ClassParameter.from(ComponentName::class.java, serviceComponent),
            ClassParameter.from(MediaBrowserCompat.ConnectionCallback::class.java, callback),
            ClassParameter.from(Bundle::class.java, rootHints)
        )
    }

    @Implementation
    protected fun connect() {
        handler.post {
            isConnected = true
            connectionCallback!!.onConnected()
        }
    }

    @Implementation
    protected fun disconnect() {
        handler.post { isConnected = false }
    }

    @Implementation
    protected fun getItem(@NonNull mediaId: String, @NonNull cb: MediaBrowserCompat.ItemCallback) {
        // mediaItem will be null when there is no MediaItem that matches the given mediaId.
        val mediaItem = mediaItems[mediaId]

        if (isConnected && mediaItem != null) {
            handler.post { cb.onItemLoaded(mediaItem) }
        } else {
            handler.post { cb.onError(mediaId) }
        }
    }

    @Implementation
    protected fun subscribe(@NonNull parentId: String, @NonNull callback: MediaBrowserCompat.SubscriptionCallback) {
        subscribe(parentId, null, callback)
    }

    @Implementation
    protected fun subscribe(
        @NonNull parentId: String,
        @Nullable options: Bundle?,
        @NonNull callback: MediaBrowserCompat.SubscriptionCallback
    ) {
        if (isConnected) {
            val parentItem = mediaItems[parentId]
            val children = if (mediaItemChildren[parentItem] == null)
                emptyList()
            else
                mediaItemChildren[parentItem]
            handler.post {
                callback.onChildrenLoaded(
                    parentId,
                    applyOptionsToResults(children, options)!!
                )
            }
        } else {
            handler.post { callback.onError(parentId) }
        }
    }

    private fun applyOptionsToResults(
        results: List<MediaItem>?,
        options: Bundle?
    ): List<MediaItem>? {
        if (results == null || options == null) {
            return results
        }
        val resultsSize = results.size
        val page = options.getInt(EXTRA_PAGE, -1)
        val pageSize = options.getInt(EXTRA_PAGE_SIZE, -1)
        if (page == -1 && pageSize == -1) {
            return results
        }

        val firstItemIndex = page * pageSize
        val lastItemIndex = firstItemIndex + pageSize
        return if (page < 0 || pageSize < 1 || firstItemIndex >= resultsSize) {
            emptyList()
        } else results.subList(firstItemIndex, Math.min(lastItemIndex, resultsSize))
    }

    /**
     * This differs from real Android search logic. Search results will contain all [ ]'s with a title that {@param query} is a substring of.
     */
    @Implementation
    protected fun search(
        @NonNull query: String,
        extras: Bundle,
        @NonNull callback: MediaBrowserCompat.SearchCallback
    ) {
        if (isConnected) {
            val searchResults = ArrayList<MediaItem>()
            for (item in mediaItems.values) {
                val mediaTitle = item.description.title!!.toString().toLowerCase()
                if (mediaTitle.contains(query.toLowerCase())) {
                    searchResults.add(item)
                }
            }
            handler.post { callback.onSearchResult(query, extras, searchResults) }
        } else {
            handler.post { callback.onError(query, extras) }
        }
    }

    /**
     * Sets the root id. Can be called more than once.
     *
     * @param mediaId the id of the root MediaItem. This MediaItem should already have been created.
     */
    fun setRootId(mediaId: String) {
        rootId = mediaId
    }

    /**
     * Creates a MediaItem and returns it.
     *
     * @param parentId the id of the parent MediaItem. If the MediaItem to be created will be the
     * root, parentId should be null.
     * @param mediaId the id of the MediaItem to be created.
     * @param title the title of the MediaItem to be created.
     * @param flag says if the MediaItem to be created is browsable and/or playable.
     * @return the newly created MediaItem.
     */
    fun createMediaItem(parentId: String?, mediaId: String, title: String, flag: Int): MediaItem {
        val metadataCompat = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId)
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, Uri.parse(mediaId).toString())
            .build()
        val mediaItem = MediaItem(metadataCompat.description, flag)
        mediaItems[mediaId] = mediaItem

        // If this MediaItem is the child of a MediaItem that has already been created. This applies to
        // all MediaItems except the root.
        if (parentId != null) {
            val parentItem = mediaItems[parentId]
            if (parentItem != null) {
                val childList = mediaItemChildren[parentItem] ?: listOf()
                mediaItemChildren[parentItem] = childList + listOf(mediaItem)
            } else {
                IllegalArgumentException("invalid parent id")
            }
        }

        return mediaItem
    }
}

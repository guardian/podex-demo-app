/*
 * Copyright 2019 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.guardian.core.mediaplayer.library

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaMetadataCompat
import com.guardian.core.mediaplayer.MediaService
import com.guardian.core.mediaplayer.extensions.album
import com.guardian.core.mediaplayer.extensions.albumArt
import com.guardian.core.mediaplayer.extensions.albumArtUri
import com.guardian.core.mediaplayer.extensions.artist
import com.guardian.core.mediaplayer.extensions.flag
import com.guardian.core.mediaplayer.extensions.id
import com.guardian.core.mediaplayer.extensions.title
import com.guardian.core.mediaplayer.extensions.urlEncoded

/**
 * Represents a tree of media that's used by [MediaService.onLoadChildren].
 *
 * [BrowseTree] maps a media id (see: [MediaMetadataCompat.METADATA_KEY_MEDIA_ID]) to one (or
 * more) [MediaMetadataCompat] objects, which are children of that media id.
 *
 * For example, given the following conceptual tree:
 * root
 *  +-- Albums
 *  |    +-- Feed_A
 *  |    |    +-- Episode_1
 *  |    |    +-- Episode_2
 *  ...
 *  +-- Artists
 *  ...
 *
 *  The [MediaMetadataCompat] class is clearly set up for music but attributes can be translated for
 *  relevance to podcasts, the "Artists" are the Creators, "Albums" are mapped to Feed Urls
 *
 *  Requesting `browseTree["root"]` would return a list that included "Albums", "Artists", and
 *  any other direct children. Taking the media ID of "Albums" ("Albums" in this example),
 *  `browseTree["Albums"]` would return a single item list "Album_A", and, finally,
 *  `browseTree["Album_A"]` would return "Song_1" and "Song_2". Since those are leaf nodes,
 *  requesting `browseTree["Song_1"]` would return null (there aren't any children of it).
 */
class BrowseTree(musicSource: MusicSource) {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadataCompat>>()

    /**
     * Whether to allow clients which are unknown (non-whitelisted) to use search on this
     * [BrowseTree].
     */
    val searchableByUnknownCaller = true

    /**
     * In this example, there's a single root node (identified by the constant
     * [UAMP_BROWSABLE_ROOT]). The root's children are each album included in the
     * [MusicSource], and the children of each album are the songs on that album.
     * (See [BrowseTree.buildAlbumRoot] for more details.)
     */
    init {
        val rootList = mediaIdToChildren[UAMP_BROWSABLE_ROOT] ?: mutableListOf()

        val recommendedMetadata = MediaMetadataCompat.Builder().apply {
            id = UAMP_RECOMMENDED_ROOT
            // todo get proper defaults
            title = "todo"
            albumArtUri = "todo"
            flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        }.build()

        val feedsMetadata = MediaMetadataCompat.Builder().apply {
            // todo get proper defaults
            id = UAMP_ALBUMS_ROOT
            title = "todo"
            albumArtUri = "todo"
            flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        }.build()

        rootList += recommendedMetadata
        rootList += feedsMetadata
        mediaIdToChildren[UAMP_BROWSABLE_ROOT] = rootList

        // todo move this to the actual source, basically construct the album nodes

        // musicSource.forEach { mediaItem ->
        //     val albumMediaId = mediaItem.album.urlEncoded
        //     val albumChildren = mediaIdToChildren[albumMediaId] ?: buildAlbumRoot(mediaItem)
        //     albumChildren += mediaItem
        //
        //
        //     // // Add the first track of each album to the 'Recommended' category
        //     // if (mediaItem.trackNumber == 1L) {
        //     //     val recommendedChildren = mediaIdToChildren[UAMP_RECOMMENDED_ROOT]
        //     //                             ?: mutableListOf()
        //     //     recommendedChildren += mediaItem
        //     //     mediaIdToChildren[UAMP_RECOMMENDED_ROOT] = recommendedChildren
        //     // }
        // }
    }

    /**
     * Provide access to the list of children with the `get` operator.
     * i.e.: `browseTree\[UAMP_BROWSABLE_ROOT\]`
     */
    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    /**
     * Builds a node, under the root, that represents an album, given
     * a [MediaMetadataCompat] object that's one of the songs on that album,
     * marking the item as [MediaItem.FLAG_BROWSABLE], since it will have child
     * node(s) AKA at least 1 song.
     */
    private fun buildAlbumRoot(mediaItem: MediaMetadataCompat): MutableList<MediaMetadataCompat> {
        val albumMetadata = MediaMetadataCompat.Builder().apply {
            id = mediaItem.album.urlEncoded
            title = mediaItem.album
            artist = mediaItem.artist
            albumArt = mediaItem.albumArt
            albumArtUri = mediaItem.albumArtUri.toString()
            flag = MediaItem.FLAG_BROWSABLE
        }.build()

        // Adds this album to the 'Albums' category.
        val rootList = mediaIdToChildren[UAMP_ALBUMS_ROOT] ?: mutableListOf()
        rootList += albumMetadata
        mediaIdToChildren[UAMP_ALBUMS_ROOT] = rootList

        // Insert the album's root with an empty list for its children, and return the list.
        return mutableListOf<MediaMetadataCompat>().also {
            mediaIdToChildren[albumMetadata.id] = it
        }
    }
}

const val UAMP_BROWSABLE_ROOT = "/"
const val UAMP_EMPTY_ROOT = "@empty@"
const val UAMP_RECOMMENDED_ROOT = "__RECOMMENDED__"
const val UAMP_ALBUMS_ROOT = "__ALBUMS__"

const val MEDIA_SEARCH_SUPPORTED = "android.media.browse.SEARCH_SUPPORTED"

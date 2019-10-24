package com.guardian.core.mediametadata

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.guardian.core.feed.Feed
import com.guardian.core.feed.FeedRepository
import com.guardian.core.feed.dao.FeedDao
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.FeedItemRepository
import com.guardian.core.mediaplayer.extensions.album
import com.guardian.core.mediaplayer.extensions.albumArtUri
import com.guardian.core.mediaplayer.extensions.artist
import com.guardian.core.mediaplayer.extensions.displayDescription
import com.guardian.core.mediaplayer.extensions.displayIconUri
import com.guardian.core.mediaplayer.extensions.displaySubtitle
import com.guardian.core.mediaplayer.extensions.displayTitle
import com.guardian.core.mediaplayer.extensions.downloadStatus
import com.guardian.core.mediaplayer.extensions.duration
import com.guardian.core.mediaplayer.extensions.flag
import com.guardian.core.mediaplayer.extensions.id
import com.guardian.core.mediaplayer.extensions.mediaUri
import com.guardian.core.mediaplayer.extensions.title
import com.guardian.core.mediaplayer.extensions.trackCount
import com.guardian.core.mediaplayer.extensions.trackNumber
import io.reactivex.Flowable
import javax.inject.Inject

class MediaMetadataRepositoryImpl
@Inject constructor(
    private val feedDao: FeedDao,
    private val feedRepository: FeedRepository,
    private val feedItemRepository: FeedItemRepository
) :
    MediaMetadataRepository {
    override fun getStoredMetadata(): Flowable<List<MediaMetadataCompat>> {
        return feedDao.getCachedFeedsWithFeedItems()
            .map { feedWithItemList ->
                feedWithItemList.flatMap {feedWithItems ->
                    val currentFeed = feedWithItems.feed

                    feedWithItems.feedItem
                        .map { feedItem ->
                            MediaMetadataCompat.Builder()
                                .from(feedItem, currentFeed, feedWithItems.feedItem.size)
                                .build()
                        }
                }
            }
    }

    override fun getMetadataForId(idString: String): Flowable<MediaMetadataCompat> {
        return feedItemRepository.getFeedItemForUrlString(idString)
            .flatMap { feedItem: FeedItem ->
                feedRepository.getFeed(feedItem.feedUrlString)
                    .map { feed ->
                        MediaMetadataCompat.Builder()
                            .from(feedItem, feed, 1)
                            .build()
                    }
            }
    }
}

private fun MediaMetadataCompat.Builder.from(feedItem: FeedItem, feed: Feed, episodeCount: Int):
    MediaMetadataCompat.Builder {
    id = feedItem.feedItemAudioUrl
    title = feedItem.title
    artist = feedItem.author
    album = feed.title
    duration = feedItem.lengthMs
    // todo genre = jsonMusic.genre
    mediaUri = feedItem.feedItemAudioUrl
    albumArtUri = feedItem.imageUrlString
    trackNumber = feedItem.episodeNumber
    trackCount = episodeCount.toLong()
    flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

    // todo move to metadata getAlbumArt = art

    // To make things easier for *displaying* these, set the display properties as well.
    displayTitle = feedItem.title
    displaySubtitle = feedItem.author
    displayDescription = feedItem.description
    displayIconUri = feedItem.imageUrlString

    // Add downloadStatus to force the creation of an "extras" bundle in the resulting
    // MediaMetadataCompat object. This is needed to send accurate metadata to the
    // media session during updates.
    downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED

    // Allow it to be used in the typical builder style.
    return this
}
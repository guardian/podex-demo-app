package com.guardian.core.mediametadata

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.guardian.core.feed.Feed
import com.guardian.core.feed.FeedRepository
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.FeedItemRepository
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
import com.guardian.core.mediaplayer.extensions.trackNumber
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class MediaMetadataRepositoryImpl
@Inject constructor(
    private val feedRepository: FeedRepository,
    private val feedItemRepository: FeedItemRepository
) :
    MediaMetadataRepository {
    override fun getMetadataForId(idString: String): Flowable<MediaMetadataCompat> {
        return Flowable.empty()
    }
}

fun MediaMetadataCompat.Builder.from(feedItem: FeedItem, feed: Feed): MediaMetadataCompat.Builder {
    id = feedItem.feedItemAudioUrl
    title = feedItem.title
    artist = feedItem.author
    // album = todo get from feed
    duration = feedItem.lengthMs
    // genre = jsonMusic.genre
    mediaUri = feedItem.feedItemAudioUrl
    albumArtUri = feedItem.imageUrlString
    trackNumber = feedItem.episodeNumber
    // trackCount = todo get from feed
    flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

    // todo move to metadata getalbumArt = art

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
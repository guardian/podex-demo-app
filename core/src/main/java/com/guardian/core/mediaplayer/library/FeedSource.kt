package com.guardian.core.mediaplayer.library

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.guardian.core.feed.FeedRepository
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.FeedItemRepository
import com.guardian.core.mediaplayer.extensions.albumArt
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FeedSource
@Inject constructor(private val feedRepository: FeedRepository,
                    private val feedItemRepository: FeedItemRepository)
    : AbstractMusicSource() {

    init {
        state = STATE_INITIALIZING
    }

    private val metaDataLiveData: MutableLiveData<List<MediaMetadataCompat>> = MutableLiveData()
    private lateinit var art: Bitmap
    private lateinit var context: Context

    fun setupGlide(context: Context) {
        this.context = context
    }

    override suspend fun load() {
        feedRepository.getFeeds().observeForever { feedList ->
            CoroutineScope(Dispatchers.IO).launch {
                art = Glide.with(context).asBitmap()
                    .load("https://interactive.guim.co.uk/podx/Puli_600.jpg")
                    .submit(
                        144,
                        144
                    )
                    .get()

                for (feed in feedList) {

                    CoroutineScope(Dispatchers.Main).launch {
                        feedItemRepository.getFeedItemsForFeed(feed)
                            .observeForever { feedItemList ->
                                //todo verify all feeds have been checked for feed items
                                // probably makes sense to use something other than livedata to return
                                // stuff from the repo
                                metaDataLiveData.setValue(
                                    (metaDataLiveData.value ?: listOf()).plus(
                                        feedItemList.map { feedItem ->
                                            MediaMetadataCompat.Builder()
                                                .from(feedItem, art)
                                                .build()
                                        }
                                    ).apply {
                                        this.forEach { Timber.i("adding to list ${it.id}") }
                                    }
                                )
                            }
                    }
                }
            }
        }
        delay(1000)
        state = STATE_INITIALIZED
    }

    override fun iterator(): Iterator<MediaMetadataCompat> {
        return metaDataLiveData.value?.iterator() ?: listOf<MediaMetadataCompat>().iterator()
    }
}


fun MediaMetadataCompat.Builder.from(feedItem: FeedItem, art: Bitmap): MediaMetadataCompat.Builder {
    id = feedItem.feedItemAudioUrl
    title = feedItem.title
    artist = feedItem.author
    //album = todo get from feed
    duration = feedItem.lengthMs
    //genre = jsonMusic.genre
    mediaUri = feedItem.feedItemAudioUrl
    albumArtUri = feedItem.imageUrlString
    trackNumber = feedItem.episodeNumber
    //trackCount = todo get from feed
    flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

    albumArt = art

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

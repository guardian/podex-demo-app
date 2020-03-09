package com.guardian.core.feed

import android.os.Build
import android.text.Html
import com.guardian.core.feed.api.GeneralFeedApi
import com.guardian.core.feed.api.xmldataobjects.FeedItemXmlDataObject
import com.guardian.core.feed.api.xmldataobjects.FeedXmlDataObject
import com.guardian.core.feed.dao.FeedDao
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.FeedItemRepository
import com.guardian.core.library.BaseRepository
import com.guardian.core.library.parseNormalPlayTimeToMillis
import com.guardian.core.library.parseNormalPlayTimeToMillisOrNull
import com.guardian.core.podxevent.OGMetadata
import com.guardian.core.podxevent.PodXEventRepository
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXSupportEvent
import com.guardian.core.podxevent.PodXWebEvent
import com.guardian.core.search.SearchResult
import io.reactivex.Flowable
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * A repository with co-routines for accessing podcast feed data from the web or from a local
 * data source.
 *
 * Feeds are generalised as [Feed] objects for consumption to the UI and identified by the
 * [Feed.feedUrlString]. The feedUrl string corresponds to the [SearchResult.feedUrlString] and can
 * therefore be treated as a foreign key.
 *
 * Individual episodes are mapped to the [FeedItem] class which in turn has associated [PodXImageEvent].
 * These can be accessed through their own repositories.
 */

class FeedRepositoryImpl
@Inject constructor(
    private val generalFeedApi: GeneralFeedApi,
    private val feedDao: FeedDao,
    private val feedItemRepository: FeedItemRepository,
    private val podXEventRepository: PodXEventRepository
) :
    FeedRepository, BaseRepository() {
    override fun getFeeds(): Flowable<List<Feed>> {
        return feedDao.getCachedFeeds()
    }

    override fun getFeed(feedUrl: String): Flowable<Feed> {
        // Fire and forget our update from the web for this feed, results will update the room repo
        // which will in turn propagate via subscription
        repositoryScope.launch {
            generalFeedApi.getFeedDeSerializedXml(feedUrl).apply {
                mapFeedObjectFromXmlFeedObjectAndCache(this, feedUrl)
            }
        }

        return feedDao.getFeedForUrlString(feedUrl)
    }

    @Suppress("DEPRECATION")
    private fun mapFeedObjectFromXmlFeedObjectAndCache(feedXmlDataObject: FeedXmlDataObject, feedUrl: String) {
        val feedImage: String = feedXmlDataObject.itunesImage.attributes["href"]?.value
            ?: feedXmlDataObject.image.url

        val feedDescription = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(feedXmlDataObject.description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(feedXmlDataObject.description)
        }

        Feed(
            title = feedXmlDataObject.title,
            author = feedXmlDataObject.author,
            description = feedDescription.toString(),
            feedUrlString = feedUrl,
            feedImageUrlString = feedImage
        ).also { feed ->
            feedDao.addFeedToCache(feed)
            mapFeedItems(feedXmlDataObject, feedImage, feedUrl)
            feedDao.addFeedToCache(feed)
        }
    }

    private fun mapFeedItems(feedXmlDataObject: FeedXmlDataObject, feedImage: String, feedUrl: String) {
        val dateFormatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault())

        feedXmlDataObject.feedItems
            .sortedBy {
                try {
                    dateFormatter.parse(it.pubDate) ?: Date(System.currentTimeMillis())
                } catch (parseException: ParseException) {
                    Timber.e(parseException.localizedMessage)
                    Date(System.currentTimeMillis())
                }
            }
            .mapIndexed { index, feedItemXmlDataObject ->
                val feedItemImage: String =
                    if (feedItemXmlDataObject.itunesImage.attributes["href"]?.value.isNullOrEmpty()) {
                        if (feedItemXmlDataObject.image.url.isEmpty()) {
                            feedImage
                        } else {
                            feedItemXmlDataObject.image.url
                        }
                    } else {
                        feedItemXmlDataObject.itunesImage.attributes["href"]?.value!!
                    }

                // todo get duration from audio file
                val pubDate = try {
                    dateFormatter.parse(feedItemXmlDataObject.pubDate) ?: Date(System.currentTimeMillis())
                } catch (parseException: ParseException) {
                    Date(System.currentTimeMillis())
                }

                val currentFeedItem = FeedItem(
                    title = feedItemXmlDataObject.title.trim(),
                    description = feedItemXmlDataObject.description.trim(),
                    imageUrlString = feedItemImage,
                    pubDate = pubDate,
                    feedItemAudioEncoding = feedItemXmlDataObject.enclosureXmlDataObject.attributes["type"]?.value ?: "",
                    feedItemAudioUrl = feedItemXmlDataObject.enclosureXmlDataObject.attributes["url"]?.value ?: "",
                    feedUrlString = feedUrl,
                    author = feedItemXmlDataObject.author.trim(),
                    episodeNumber = index.toLong(),
                    lengthMs = feedItemXmlDataObject.duration.parseNormalPlayTimeToMillisOrNull() ?: 0L
                )

                // prevent duplicate podX events from being added
                podXEventRepository.deletePodXEventsForFeedItem(currentFeedItem)
                mapPodXImages(feedItemXmlDataObject)
                mapPodXWebLinks(feedItemXmlDataObject)
                mapPodXSupport(feedItemXmlDataObject)

                currentFeedItem
            }.also { feedItems ->
                feedItemRepository.addFeedItems(feedItems)
                Timber.i("Caching feed items ${feedItems.size}")
            }
    }

    private fun mapPodXWebLinks(feedItemXmlDataObject: FeedItemXmlDataObject) {
        feedItemXmlDataObject.podxWeb.filter { podXEventXmlDataObject ->
            podXEventXmlDataObject.start.parseNormalPlayTimeToMillisOrNull() != null
        }.map { podXWebEventXmlDataObject ->
            // set a placeholder as we will scrape metadata afterwards
            // todo fetch asynchronously
            val urlString = podXWebEventXmlDataObject.attributes["url"]?.value ?: ""
            val placeholderMetadata = try {
                OGMetadata
                    .extractOGMetadataFromUrlString(urlString)
            } catch (exception: IllegalArgumentException) {
                Timber.w("could not extract og data for $urlString")
                OGMetadata("", "", "", "")
            }

            PodXWebEvent(
                urlString = urlString,
                timeStart = podXWebEventXmlDataObject.start.parseNormalPlayTimeToMillis(),
                timeEnd = podXWebEventXmlDataObject.end.parseNormalPlayTimeToMillisOrNull()
                    ?: podXWebEventXmlDataObject.start.parseNormalPlayTimeToMillis(),
                caption = podXWebEventXmlDataObject.caption.trim(),
                notification = podXWebEventXmlDataObject.notification.trim(),
                feedItemUrlString = feedItemXmlDataObject.enclosureXmlDataObject.attributes["url"]?.value ?: "",
                ogMetadata = placeholderMetadata
            )
        }.also { podXEventList ->
            if (podXEventList.isNotEmpty()) {
                podXEventRepository.addPodXWebEvents(podXEventList)
                Timber.i("Caching PodxWebEvents ${podXEventList.size}")
            }
        }
    }

    private fun mapPodXImages(feedItemXmlDataObject: FeedItemXmlDataObject) {
        feedItemXmlDataObject.podxImages.filter { podXEventXmlDataObject ->
            podXEventXmlDataObject.start.parseNormalPlayTimeToMillisOrNull() != null
        }.map { podXImageEventXmlDataObject ->
            PodXImageEvent(
                urlString = podXImageEventXmlDataObject.attributes["href"]?.value ?: "",
                timeStart = podXImageEventXmlDataObject.start.parseNormalPlayTimeToMillis(),
                timeEnd = podXImageEventXmlDataObject.end.parseNormalPlayTimeToMillisOrNull()
                    ?: podXImageEventXmlDataObject.start.parseNormalPlayTimeToMillis(),
                caption = podXImageEventXmlDataObject.caption.trim(),
                notification = podXImageEventXmlDataObject.notification.trim(),
                feedItemUrlString = feedItemXmlDataObject.enclosureXmlDataObject.attributes["url"]?.value ?: ""
            )
        }.also { podXEventList ->
            if (podXEventList.isNotEmpty()) {
                podXEventRepository.addPodXImageEvents(podXEventList)
                Timber.i("Caching PodxImageEvents ${podXEventList.size}")
            }
        }
    }

    private fun mapPodXSupport(feedItemXmlDataObject: FeedItemXmlDataObject) {
        feedItemXmlDataObject.podxSupport.filter { podXEventXmlDataObject ->
            podXEventXmlDataObject.start.parseNormalPlayTimeToMillisOrNull() != null
        }.map { podXSupportEventXmlDataObject ->
            PodXSupportEvent(
                timeStart = podXSupportEventXmlDataObject.start.parseNormalPlayTimeToMillis(),
                timeEnd = podXSupportEventXmlDataObject.end.parseNormalPlayTimeToMillisOrNull()
                    ?: podXSupportEventXmlDataObject.start.parseNormalPlayTimeToMillis(),
                caption = podXSupportEventXmlDataObject.caption.trim(),
                notification = podXSupportEventXmlDataObject.notification.trim(),
                feedItemUrlString = feedItemXmlDataObject.enclosureXmlDataObject.attributes["url"]?.value ?: ""
            )
        }.also { podXEventList ->
            if (podXEventList.isNotEmpty()) {
                podXEventRepository.addPodXSupportEvents(podXEventList)
                Timber.i("Caching PodxSupportEvents ${podXEventList.size}")
            }
        }
    }

    private fun mapPodXCallPromptEvent(feedItemXmlDataObject: FeedItemXmlDataObject) {
        feedItemXmlDataObject.podxSupport.filter { podXEventXmlDataObject ->
            podXEventXmlDataObject.start.parseNormalPlayTimeToMillisOrNull() != null
        }.map { podXSupportEventXmlDataObject ->
            PodXSupportEvent(
                timeStart = podXSupportEventXmlDataObject.start.parseNormalPlayTimeToMillis(),
                timeEnd = podXSupportEventXmlDataObject.end.parseNormalPlayTimeToMillisOrNull()
                    ?: podXSupportEventXmlDataObject.start.parseNormalPlayTimeToMillis(),
                caption = podXSupportEventXmlDataObject.caption.trim(),
                notification = podXSupportEventXmlDataObject.notification.trim(),
                feedItemUrlString = feedItemXmlDataObject.enclosureXmlDataObject.attributes["url"]?.value ?: ""
            )
        }.also { podXEventList ->
            if (podXEventList.isNotEmpty()) {
                podXEventRepository.addPodXSupportEvents(podXEventList)
                Timber.i("Caching PodxSupportEvents ${podXEventList.size}")
            }
        }
    }
}
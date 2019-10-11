package com.guardian.core.feed

import androidx.lifecycle.LiveData
import com.guardian.core.feed.api.FeedXmlDataObject
import com.guardian.core.feed.api.GeneralFeedApi
import com.guardian.core.feed.dao.FeedDao
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.dao.FeedItemDao
import com.guardian.core.search.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
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
 * Individual episodes are mapped to the [FeedItem] class which in turn has associated [PodXEvent]
 * objects which are all returned from this repository at this time.
 */

class FeedRepositoryImpl
@Inject constructor(
    val generalFeedApi: GeneralFeedApi,
    val feedDao: FeedDao,
    val feedItemDao: FeedItemDao
) :
    FeedRepository {
    override fun getFeeds(): LiveData<List<Feed>> {
        return feedDao.getCachedFeeds()
    }

    override suspend fun getFeed(feedUrl: String): LiveData<Feed> {
        withContext(Dispatchers.IO) {
            generalFeedApi.getFeedDeSerializedXml(feedUrl).apply {
                mapFeedObjectFromXmlFeedObjectAndCache(this, feedUrl)
            }
        }

        return feedDao.getFeedForUrlString(feedUrl)
    }

    private fun mapFeedObjectFromXmlFeedObjectAndCache(feedXmlDataObject: FeedXmlDataObject, feedUrl: String) {
        val feedImage: String = feedXmlDataObject.itunesImage.attributes["href"]?.value
            ?: feedXmlDataObject.image.url

        val dateFormatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault())

        feedXmlDataObject.feedItems.map {
            val feedItemImage: String = it.itunesImage.attributes["href"]?.value
                ?: it.image.url

            FeedItem(
                title = it.title,
                description = it.description,
                imageUrlString = feedItemImage,
                pubDate = dateFormatter.parse(it.pubDate) ?: Date(System.currentTimeMillis()),
                feedItemAudioEncoding = it.enclosureXmlDataObject.attributes["type"]?.value ?: "",
                feedItemAudioUrl = it.enclosureXmlDataObject.attributes["url"]?.value ?: "",
                feedUrlString = feedUrl
            )
        }.also {
            feedItems -> feedItemDao.addFeedList(feedItems)
            Timber.i("Caching feed items ${feedItems.size}")
        }

        Feed(
            title = feedXmlDataObject.title,
            description = feedXmlDataObject.description,
            feedUrlString = feedUrl,
            feedImageUrlString = feedImage
        ).also {
            feed -> feedDao.addFeedToCache(feed)
        }
    }
}
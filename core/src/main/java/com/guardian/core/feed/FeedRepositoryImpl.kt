package com.guardian.core.feed

import com.guardian.core.feed.api.FeedXmlDataObject
import com.guardian.core.feed.api.GeneralFeedApi
import com.guardian.core.search.SearchResult
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
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
@Inject constructor(val generalFeedApi: GeneralFeedApi)
    : FeedRepository {
    override suspend fun getFeed(feedUrl: String): Feed {
        var feed: Feed? = null

        //todo get feed from repo

        try {
            val feedApiObject = generalFeedApi.getFeedDeSerializedXml(feedUrl)

            feed = mapFeedObjectFromXmlFeedObject(feedApiObject)
        } catch (apiError: Throwable) {
            Timber.e(apiError)
        }

        if (feed == null) {
            Timber.e("Could not get feed at $feedUrl")
        }

        return feed ?: Feed("", "", "", "", listOf())
    }

    private fun mapFeedObjectFromXmlFeedObject(feedXmlDataObject: FeedXmlDataObject) : Feed {
        val feedImage : String = feedXmlDataObject.itunesImage.attributes["href"]?.value
            ?: feedXmlDataObject.image.url

        val dateFormatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault())



        return Feed(
            title = feedXmlDataObject.title,
            description = feedXmlDataObject.description,
            feedUrlString = feedXmlDataObject.link,
            feedImageUrlString = feedImage,
            feedItems = feedXmlDataObject.feedItems.map {
                val feedItemImage: String = it.itunesImage.attributes["href"]?.value
                    ?: it.image.url

                FeedItem(
                    title = it.title,
                    description = it.description,
                    imageUrlString = feedItemImage,
                    pubDate = dateFormatter.parse(it.pubDate) ?: Date(System.currentTimeMillis())
                )
            }
        )

    }

}
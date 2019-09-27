package com.guardian.core.feed

import javax.inject.Inject
import com.guardian.core.search.SearchResult

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
@Inject constructor()
    : FeedRepository {

}
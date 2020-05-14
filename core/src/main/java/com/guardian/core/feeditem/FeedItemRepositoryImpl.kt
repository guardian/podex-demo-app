package com.guardian.core.feeditem

import com.guardian.core.feed.Feed
import com.guardian.core.feeditem.dao.FeedItemDao
import io.reactivex.Flowable
import io.reactivex.rxkotlin.zipWith
import java.util.Date
import javax.inject.Inject

class FeedItemRepositoryImpl @Inject constructor(
    private val feedItemDao: FeedItemDao
)
    : FeedItemRepository {
    override fun addFeedItems(feedItems: List<FeedItem>) {
        feedItemDao.addFeedList(feedItems)
    }

    override fun getFeedItemsForFeed(feed: Feed): Flowable<List<FeedItem>> {
        return feedItemDao.getFeedItemsForFeedUrl(feed.feedUrlString)
    }

    override fun getFeedItemForUrlString(feedItemUrlString: String): Flowable<FeedItem> {
        return feedItemDao.getFeedItemForUrlString(feedItemUrlString)
    }

    override fun getFeedItemForSearchParams(
        feedItemTitle: String?,
        feedItemPubDate: Date?,
        feedItemGuid: String?,
        feedItemAudioTime: Long?,
        feedImageUrlString: String?
    ): Flowable<List<FeedItem>> {
        val unMergedFlowables: MutableList<Flowable<List<FeedItem>>> = mutableListOf()
        if (feedItemTitle != null) {
            unMergedFlowables.add(feedItemDao.getFeedItemsWithTitle(feedItemTitle))
        }

        if (feedItemPubDate != null) {
            unMergedFlowables.add(feedItemDao.getFeedItemsWithPubDate(feedItemPubDate))
        }

        if (feedItemGuid != null) {
            unMergedFlowables.add(feedItemDao.getFeedItemsWithGuid(feedItemGuid))
        }

        if (feedItemAudioTime != null) {
            unMergedFlowables.add(feedItemDao.getFeedItemsWithPlayTime(feedItemAudioTime))
        }

        if (feedImageUrlString != null) {
            unMergedFlowables.add(feedItemDao.getFeedItemsWithImage(feedImageUrlString))
        }

        var aggregateFlowable: Flowable<List<FeedItem>> = Flowable.empty()
        for (repoFlowable in unMergedFlowables) {
            aggregateFlowable = aggregateFlowable.zipWith(repoFlowable, {aggregateList, repoList ->
                aggregateList + repoList
            })
        }



        val mergedFlowable = aggregateFlowable.map { aggregateList ->
            aggregateList
                .toSortedSet(Comparator { o1, o2 ->
                    aggregateList.count { it.feedItemAudioUrl == o1.feedItemAudioUrl } -
                        aggregateList.count{ it.feedItemAudioUrl == o2.feedItemAudioUrl }
                })
                .toList()
        }

        return mergedFlowable
    }
}
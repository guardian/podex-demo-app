package com.guardian.core.feed.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.feed.Feed
import com.guardian.core.feed.FeedWithItems
import io.reactivex.Flowable

@Dao
interface FeedDao {
    @Query("SELECT * from feeds")
    fun getCachedFeeds(): Flowable<List<Feed>>

    @Query("SELECT * from feeds WHERE feedUrlString = :url")
    fun getFeedForUrlString(url: String): Flowable<Feed>

    @Transaction
    @Query("SELECT * from feeds")
    fun getCachedFeedsWithFeedItems(): Flowable<List<FeedWithItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFeedToCache(feed: Feed)

    /**
     * Since all [FeedItem]s and [PodXEvent]s are children of feed, we can delete the feed, and the
     * associated data will be cleaned up
     */
    @Delete
    fun deleteFeed(feed: Feed)
}

package com.guardian.core.feed.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guardian.core.feed.Feed
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface FeedDao {
    @Query("SELECT * from feeds")
    fun getCachedFeeds(): Flowable<List<Feed>>

    @Query("SELECT * from feeds WHERE feedUrlString = :url")
    fun getFeedForUrlString(url: String): Flowable<Feed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFeedToCache(feed: Feed): Completable
}
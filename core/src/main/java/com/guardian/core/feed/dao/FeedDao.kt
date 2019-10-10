package com.guardian.core.feed.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.feed.Feed

@Dao
interface FeedDao {
    @Transaction
    @Query("SELECT * from feeds")
    fun getCachedFeeds() : LiveData<List<Feed>>

    @Transaction
    @Query("SELECT * from feeds WHERE feedUrlString = :url")
    fun getFeedForUrlString(url: String): LiveData<Feed>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFeedToCache(feed: Feed)
}
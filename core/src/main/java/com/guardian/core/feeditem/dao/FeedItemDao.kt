package com.guardian.core.feeditem.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guardian.core.feeditem.FeedItem

@Dao
interface FeedItemDao {
    @Query("SELECT * from feed_items WHERE feedItemAudioUrl = :url")
    fun getFeedItemForUrlString(url: String): LiveData<FeedItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFeedList(feedItems: List<FeedItem>)
}
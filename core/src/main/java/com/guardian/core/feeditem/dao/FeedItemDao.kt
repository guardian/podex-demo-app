package com.guardian.core.feeditem.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guardian.core.feeditem.FeedItem
import io.reactivex.Flowable

@Dao
interface FeedItemDao {
    @Query("SELECT * from feed_items WHERE feedItemAudioUrl = :url LIMIT 1")
    fun getFeedItemForUrlString(url: String): Flowable<FeedItem>

    @Query("SELECT * from feed_items WHERE feedUrlString = :feedUrlString")
    fun getFeedItemsForFeedUrl(feedUrlString: String): Flowable<List<FeedItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFeedList(feedItems: List<FeedItem>)
}
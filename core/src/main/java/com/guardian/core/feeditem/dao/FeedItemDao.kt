package com.guardian.core.feeditem.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guardian.core.feeditem.FeedItem
import io.reactivex.Flowable
import java.util.Date

@Dao
interface FeedItemDao {
    @Query("SELECT * from feed_items WHERE feedItemAudioUrl = :url LIMIT 1")
    fun getFeedItemForUrlString(url: String): Flowable<FeedItem>

    @Query("SELECT * from feed_items WHERE feedUrlString = :feedUrlString")
    fun getFeedItemsForFeedUrl(feedUrlString: String): Flowable<List<FeedItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFeedList(feedItems: List<FeedItem>)

    @Query("SELECT * from feed_items WHERE title = :feedItemTitle")
    fun getFeedItemsWithTitle(feedItemTitle: String): Flowable<List<FeedItem>>

    @Query("SELECT * from feed_items WHERE pubDate = :feedItemPubDate")
    fun getFeedItemsWithPubDate(feedItemPubDate: Date): Flowable<List<FeedItem>>

    @Query("SELECT * from feed_items WHERE guid = :feedItemGuid")
    fun getFeedItemsWithGuid(feedItemGuid: String): Flowable<List<FeedItem>>

    @Query("SELECT * from feed_items WHERE lengthMs = :feedItemAudioTime")
    fun getFeedItemsWithPlayTime(feedItemAudioTime: Long): Flowable<List<FeedItem>>

    @Query("SELECT * from feed_items WHERE imageUrlString = :feedImageUrlString")
    fun getFeedItemsWithImage(feedImageUrlString: String): Flowable<List<FeedItem>>
}
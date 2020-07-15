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

    @Query("SELECT * FROM feed_items WHERE (:feedItemEnclosureUrl is NULL OR feedItemAudioUrl = :feedItemEnclosureUrl) OR " +
        "(:feedItemTitle is NULL OR title LIKE :feedItemTitle) AND " +
        "(:feedItemGuid is NULL OR guid LIKE :feedItemGuid) AND " +
        "(:feedItemPubDate is NULL OR pubDate LIKE :feedItemPubDate)")
    fun getFeedItemsWithLinkParams(
        feedItemTitle: String? = null,
        feedItemGuid: String? = null,
        feedItemEnclosureUrl: String? = null,
        feedItemPubDate: Date? = null
    ): Flowable<List<FeedItem>>

}
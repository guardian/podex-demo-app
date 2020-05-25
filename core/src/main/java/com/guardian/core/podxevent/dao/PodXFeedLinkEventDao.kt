package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.podxevent.PodXFeedLinkEvent
import io.reactivex.Flowable

@Dao
interface PodXFeedLinkEventDao {
    @Query("SELECT * from podx_feed_link_events")
    fun getPodXFeedLinkEvents(): Flowable<List<PodXFeedLinkEvent>>

    @Query("SELECT * from podx_feed_link_events where currentFeedItemUrlString = :feedItemAudioUrl")
    fun getPodXFeedLinkEventsForFeedItemUrl(feedItemAudioUrl: String): Flowable<List<PodXFeedLinkEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun putPodXFeedLinkEvent(podXFeedLinkEvent: PodXFeedLinkEvent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun putPodXFeedLinkEventList(podXFeedLinkEvent: List<PodXFeedLinkEvent>)

    @Transaction
    @Query("DELETE from podx_feed_link_events where currentFeedItemUrlString = :feedItemAudioUrl")
    fun removePodXFeedLinkEventList(feedItemAudioUrl: String)
}
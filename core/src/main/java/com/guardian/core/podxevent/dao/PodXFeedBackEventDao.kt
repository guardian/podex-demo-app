package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.podxevent.PodXFeedBackEvent
import io.reactivex.Flowable

@Dao
interface PodXFeedBackEventDao {
    @Query("SELECT * from podx_feed_back_events")
    fun getPodXFeedBackEvents(): Flowable<List<PodXFeedBackEvent>>

    @Query("SELECT * from podx_feed_back_events where feedItemUrlString = :feedItemAudioUrl")
    fun getPodXFeedBackEventsForFeedItemUrl(feedItemAudioUrl: String): Flowable<List<PodXFeedBackEvent>>

    @Insert
    fun putPodXFeedBackEvent(podXFeedBackEvent: PodXFeedBackEvent)

    @Insert
    fun putPodXFeedBackEventList(podXFeedBackEvent: List<PodXFeedBackEvent>)

    @Transaction
    @Query("DELETE from podx_feed_back_events where feedItemUrlString = :feedItemAudioUrl")
    fun removePodXFeedBackEventList(feedItemAudioUrl: String)
}

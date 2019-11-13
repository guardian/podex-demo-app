package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.podxevent.PodXWebEvent
import io.reactivex.Flowable

@Dao
interface PodXWebEventDao {
    @Query("SELECT * from podx_web_events")
    fun getPodXWebEvents(): Flowable<List<PodXWebEvent>>

    @Query("SELECT * from podx_web_events where feedItemUrlString = :feedItemAudioUrl")
    fun getPodXWebEventsForFeedItemUrl(feedItemAudioUrl: String): Flowable<List<PodXWebEvent>>

    @Insert
    fun putPodXWebEvent(podXWebEvent: PodXWebEvent)

    @Insert
    fun putPodXWebEventList(podXWebEvent: List<PodXWebEvent>)

    @Transaction
    @Query("DELETE from podx_web_events where feedItemUrlString = :feedItemAudioUrl")
    fun removePodXWebEventList(feedItemAudioUrl: String)
}
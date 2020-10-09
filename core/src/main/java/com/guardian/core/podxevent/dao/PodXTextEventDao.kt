package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.podxevent.PodXTextEvent
import io.reactivex.Flowable

@Dao
interface PodXTextEventDao {
    @Query("SELECT * from podx_text_events")
    fun getPodXTextEvents(): Flowable<List<PodXTextEvent>>

    @Query("SELECT * from podx_text_events where feedItemUrlString = :feedItemAudioUrl")
    fun getPodXTextEventsForFeedItemUrl(feedItemAudioUrl: String): Flowable<List<PodXTextEvent>>

    @Insert
    fun putPodXTextEvent(podXTextEvent: PodXTextEvent)

    @Insert
    fun putPodXTextEventList(podXTextEvent: List<PodXTextEvent>)

    @Transaction
    @Query("DELETE from podx_text_events where feedItemUrlString = :feedItemAudioUrl")
    fun removePodXTextEventList(feedItemAudioUrl: String)
}

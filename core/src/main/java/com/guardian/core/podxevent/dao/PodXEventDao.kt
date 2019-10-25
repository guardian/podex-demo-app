package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.guardian.core.podxevent.PodXEvent
import io.reactivex.Flowable

@Dao
interface PodXEventDao {
    @Query("SELECT * from podx_events")
    fun getPodXEvents(): Flowable<List<PodXEvent>>

    @Query("SELECT * from podx_events where feedItemUrlString = :feedItemAudioUrl")
    fun getPodXEventsForFeedItemUrl(feedItemAudioUrl: String): Flowable<List<PodXEvent>>

    @Insert
    fun putPodxEvent(podXEvent: PodXEvent)

    @Insert
    fun putPodxEventList(podXEvent: List<PodXEvent>)
}
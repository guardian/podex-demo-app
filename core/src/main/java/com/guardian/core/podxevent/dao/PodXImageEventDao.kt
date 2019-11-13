package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.podxevent.PodXImageEvent
import io.reactivex.Flowable

@Dao
interface PodXImageEventDao {
    @Query("SELECT * from podx_image_events")
    fun getPodXImageEvents(): Flowable<List<PodXImageEvent>>

    @Query("SELECT * from podx_image_events where feedItemUrlString = :feedItemAudioUrl")
    fun getPodXImageEventsForFeedItemUrl(feedItemAudioUrl: String): Flowable<List<PodXImageEvent>>

    @Insert
    fun putPodXImageEvent(podXImageEvent: PodXImageEvent)

    @Insert
    fun putPodXImageEventList(podXImageEvent: List<PodXImageEvent>)

    @Transaction
    @Query("DELETE from podx_image_events where feedItemUrlString = :feedItemAudioUrl")
    fun removePodXImageEventList(feedItemAudioUrl: String)
}
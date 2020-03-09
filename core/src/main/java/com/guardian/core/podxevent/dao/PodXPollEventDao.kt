package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.podxevent.PodXPollEvent
import io.reactivex.Flowable

@Dao
interface PodXPollEventDao {
    @Query("SELECT * from podx_poll_events")
    fun getPodXPollEvents(): Flowable<List<PodXPollEvent>>

    @Query("SELECT * from podx_poll_events where feedItemUrlString = :feedItemAudioUrl")
    fun getPodXPollEventsForFeedItemUrl(feedItemAudioUrl: String): Flowable<List<PodXPollEvent>>

    @Insert
    fun putPodXPollEvent(podXPollEvent: PodXPollEvent)

    @Insert
    fun putPodXPollEventList(podXPollEvent: List<PodXPollEvent>)

    @Transaction
    @Query("DELETE from podx_poll_events where feedItemUrlString = :feedItemAudioUrl")
    fun removePodXPollEventList(feedItemAudioUrl: String)
}
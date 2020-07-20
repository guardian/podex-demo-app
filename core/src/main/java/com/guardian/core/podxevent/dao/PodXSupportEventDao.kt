package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.podxevent.PodXSupportEvent
import io.reactivex.Flowable

@Dao
interface PodXSupportEventDao {
    @Query("SELECT * from podx_support_events")
    fun getPodXSupportEvents(): Flowable<List<PodXSupportEvent>>

    @Query("SELECT * from podx_support_events where feedItemUrlString = :feedItemAudioUrl")
    fun getPodXSupportEventsForFeedItemUrl(feedItemAudioUrl: String): Flowable<List<PodXSupportEvent>>

    @Insert
    fun putPodXSupportEvent(podXSupportEvent: PodXSupportEvent)

    @Insert
    fun putPodXSupportEventList(podXSupportEventList: List<PodXSupportEvent>)

    @Transaction
    @Query("DELETE from podx_support_events where feedItemUrlString = :feedItemAudioUrl")
    fun removePodXSupportEventList(feedItemAudioUrl: String)
}

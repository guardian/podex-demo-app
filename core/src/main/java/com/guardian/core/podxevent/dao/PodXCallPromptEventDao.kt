package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.podxevent.PodXCallPromptEvent
import io.reactivex.Flowable

@Dao
interface PodXCallPromptEventDao {
    @Query("SELECT * from podx_call_prompt_events")
    fun getPodXCallPromptEvents(): Flowable<List<PodXCallPromptEvent>>

    @Query("SELECT * from podx_call_prompt_events where feedItemUrlString = :feedItemAudioUrl")
    fun getPodXCallPromptEventsForFeedItemUrl(feedItemAudioUrl: String): Flowable<List<PodXCallPromptEvent>>

    @Insert
    fun putPodXCallPromptEvent(podXCallPromptEvent: PodXCallPromptEvent)

    @Insert
    fun putPodXCallPromptEventList(podXCallPromptEvent: List<PodXCallPromptEvent>)

    @Transaction
    @Query("DELETE from podx_call_prompt_events where feedItemUrlString = :feedItemAudioUrl")
    fun removePodXCallPromptEventList(feedItemAudioUrl: String)
}

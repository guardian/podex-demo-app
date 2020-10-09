package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.podxevent.PodXSocialPromptEvent
import io.reactivex.Flowable

@Dao
interface PodXSocialPromptEventDao {
    @Query("SELECT * from podx_social_prompt_events")
    fun getPodXSocialPromptEvents(): Flowable<List<PodXSocialPromptEvent>>

    @Query("SELECT * from podx_social_prompt_events where feedItemUrlString = :feedItemAudioUrl")
    fun getPodXSocialPromptEventsForFeedItemUrl(feedItemAudioUrl: String): Flowable<List<PodXSocialPromptEvent>>

    @Insert
    fun putPodXSocialPromptEvent(podXSocialPromptEvent: PodXSocialPromptEvent)

    @Insert
    fun putPodXSocialPromptEventList(podXSocialPromptEvent: List<PodXSocialPromptEvent>)

    @Transaction
    @Query("DELETE from podx_social_prompt_events where feedItemUrlString = :feedItemAudioUrl")
    fun removePodXSocialPromptEventList(feedItemAudioUrl: String)
}

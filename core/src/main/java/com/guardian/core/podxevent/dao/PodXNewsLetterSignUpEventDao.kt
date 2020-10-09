package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.guardian.core.podxevent.PodXNewsLetterSignUpEvent
import io.reactivex.Flowable

@Dao
interface PodXNewsLetterSignUpEventDao {
    @Query("SELECT * from podx_news_letter_sign_up_events")
    fun getPodXNewsLetterSignUpEvents(): Flowable<List<PodXNewsLetterSignUpEvent>>

    @Query("SELECT * from podx_news_letter_sign_up_events where feedItemUrlString = :feedItemAudioUrl")
    fun getPodXNewsLetterSignUpEventsForFeedItemUrl(feedItemAudioUrl: String): Flowable<List<PodXNewsLetterSignUpEvent>>

    @Insert
    fun putPodXNewsLetterSignUpEvent(podXNewsLetterSignUpEvent: PodXNewsLetterSignUpEvent)

    @Insert
    fun putPodXNewsLetterSignUpEventList(podXNewsLetterSignUpEvent: List<PodXNewsLetterSignUpEvent>)

    @Transaction
    @Query("DELETE from podx_news_letter_sign_up_events where feedItemUrlString = :feedItemAudioUrl")
    fun removePodXNewsLetterSignUpEventList(feedItemAudioUrl: String)
}

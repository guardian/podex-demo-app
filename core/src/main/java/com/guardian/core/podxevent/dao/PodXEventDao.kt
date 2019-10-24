package com.guardian.core.podxevent.dao

import androidx.room.Dao
import androidx.room.Query
import com.guardian.core.podxevent.PodXEvent
import io.reactivex.Flowable

@Dao
interface PodXEventDao {
    @Query("SELECT * from podx_events")
    fun getPodXEvents(): Flowable<List<PodXEvent>>
}
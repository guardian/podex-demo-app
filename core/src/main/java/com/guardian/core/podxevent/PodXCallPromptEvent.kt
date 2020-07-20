package com.guardian.core.podxevent

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "podx_call_prompt_events",
    indices = [Index(value = ["feedItemUrlString"], unique = false)]
)
@Parcelize
class PodXCallPromptEvent(
    val timeStart: Long,
    val timeEnd: Long,
    val caption: String,
    val notification: String,
    val feedItemUrlString: String,
    val phoneNumber: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable

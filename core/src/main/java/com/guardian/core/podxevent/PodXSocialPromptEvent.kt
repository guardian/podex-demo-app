package com.guardian.core.podxevent

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "podx_social_prompt_events",
    indices = [Index(value = ["feedItemUrlString"], unique = false)]
)
@Parcelize
data class PodXSocialPromptEvent(
    val timeStart: Long,
    val timeEnd: Long,
    val socialLinkUrlString: String,
    val caption: String,
    val notification: String,
    val feedItemUrlString: String,
    @Embedded
    var metadata: Metadata,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable

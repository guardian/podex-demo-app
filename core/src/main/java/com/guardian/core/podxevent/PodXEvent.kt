package com.guardian.core.podxevent

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.atomic.AtomicBoolean

@Entity(tableName = "podx_events",
    indices = [Index(value = ["feedItemUrlString"], unique = false)])
@Parcelize
data class PodXEvent(
    val type: PodXType,
    val timeStart: Long,
    val timeEnd: Long,
    val urlString: String,
    val caption: String,
    val notification: String,
    val feedItemUrlString: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val handled: AtomicBoolean = AtomicBoolean(false)
) : Parcelable

enum class PodXType {
    IMAGE,
    DONATION,
    WEB
}
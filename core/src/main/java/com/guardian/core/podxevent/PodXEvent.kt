package com.guardian.core.podxevent

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

//TODO maybe rather than generic podx events for everything extend the base podx event with a few fields for specific events
@Entity(tableName="podx_events",
    indices = [Index(value = ["feedItemUrlString"], unique = false)])
data class PodXEvent (
    val type: PodXType,
    val timeStart: Long,
    val timeEnd: Long,
    val urlString: String,
    val caption: String,
    val notification: String,
    val feedItemUrlString: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

enum class PodXType {
    IMAGE,
    DONATION,
    WEB
}
package com.guardian.core.podxevent

import androidx.room.Entity

//TODO maybe rather than generic podx events for everything extend the base podx event with a few fields for specific events
@Entity(tableName="podx_events")
data class PodXEvent (
    val type: PodxType,
    val timeStart: Long,
    val timeEnd: Long,
    val urlString: String,
    val caption: String,
    val notification: String
)

enum class PodxType {
    IMAGE,
    DONATION,
    WEB
}
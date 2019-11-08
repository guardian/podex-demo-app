package com.guardian.core.feed

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feeds")
data class Feed(
    @PrimaryKey
    val feedUrlString: String,
    val title: String,
    val feedImageUrlString: String,
    val description: String
)
package com.guardian.core.feed

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.guardian.core.feeditem.FeedItem

@Entity(tableName = "feeds")
@ForeignKey(entity = FeedItem::class,
    parentColumns = ["feedUrlString"],
    childColumns = ["feedUrlString"],
    onDelete = ForeignKey.CASCADE
)
data class Feed(
    @PrimaryKey
    val feedUrlString: String,
    val title: String,
    val author: String,
    val feedImageUrlString: String,
    val description: String
)
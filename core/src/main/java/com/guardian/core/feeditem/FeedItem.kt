package com.guardian.core.feeditem

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.guardian.core.feed.Feed
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "feed_items",
    indices = [Index(value = ["feedUrlString"], unique = false)])
@ForeignKey(entity = Feed::class,
    parentColumns = ["feedUrlString"],
    childColumns = ["feedUrlString"],
    onDelete = CASCADE)
data class FeedItem(
    val title: String,
    val description: String,
    val imageUrlString: String,
    val feedUrlString: String,
    @PrimaryKey
    val feedItemAudioUrl: String,
    val feedItemAudioEncoding: String,
    val pubDate: Date,
    val author: String,
    val lengthMs: Long,
    val episodeNumber: Long
) : Parcelable

package com.guardian.core.feeditem

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.guardian.core.podxevent.PodXImageEvent
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "feed_items",
    indices = [Index(value = ["feedUrlString"], unique = false),
        Index(value = ["title"], unique = false),
        Index(value = ["guid"], unique = false),
        Index(value = ["pubDate"], unique = false)])
@ForeignKey(entity = PodXImageEvent::class,
    parentColumns = ["feedItemAudioUrl"],
    childColumns = ["feedItemUrlString"],
    onDelete = ForeignKey.CASCADE)
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
    val guid: String,
    val lengthMs: Long,
    val episodeNumber: Long
) : Parcelable

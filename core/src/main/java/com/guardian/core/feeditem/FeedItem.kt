package com.guardian.core.feeditem

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.guardian.core.feed.Feed
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "feed_items")
@ForeignKey(entity = Feed::class,
    parentColumns = ["feedUrlString"],
    childColumns = ["feedUrlString"])
data class FeedItem(
    val title: String,
    val description: String,
    val imageUrlString: String,
    val feedUrlString: String,
    @PrimaryKey
    val feedItemAudioUrl: String,
    val feedItemAudioEncoding: String,
    val pubDate: Date
) : Parcelable

package com.guardian.core.podxevent

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Entity(
    tableName = "podx_feed_link_events",
    indices = [Index(value = ["currentFeedItemUrlString"], unique = false)]
)
@Parcelize
data class PodXFeedLinkEvent(
    val timeStart: Long,
    val timeEnd: Long,
    val caption: String,
    val notification: String,
    val currentFeedItemUrlString: String,
    val remoteFeedUrlString: String,
    val remoteFeedItemUrlString: String?,
    val remoteFeedItemTitle: String?,
    val remoteFeedItemPubDate: Date?,
    val remoteFeedItemGuid: String?,
    val remoteItemAudioTime: Long,
    val remoteFeedImageUrlString: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable

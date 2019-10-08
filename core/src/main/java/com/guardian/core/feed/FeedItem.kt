package com.guardian.core.feed

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class FeedItem(
    val title: String,
    val description: String,
    val imageUrlString: String,
    // val feedAudioUrl: String,
    // val feedAudioEncoding: String,
    val pubDate: Date
) : Parcelable

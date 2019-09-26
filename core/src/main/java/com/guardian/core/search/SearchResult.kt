package com.guardian.core.search

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

/**
 * The data class that represents the result from an api that returns information about a podcast
 * feed.
 */

@Parcelize
data class SearchResult (
    val title : String,
    val imageUrlString : String,
    val feedUrlString : String
) : Parcelable
package com.guardian.core.search.api

import com.google.gson.annotations.SerializedName

data class SearchItunesApiDataObject (
    @SerializedName("collectionName") val name: String?,
    @SerializedName("feedUrl") val feedUrlString: String?,
    @SerializedName("artworkUrl30") val atworkUrl30String: String?,
    @SerializedName("artworkUrl60") val atworkUrl60String: String?,
    @SerializedName("artworkUrl100") val atworkUrl100String: String?,
    @SerializedName("artworkUrl600") val atworkUrl600String: String?
)

package com.guardian.core.search.api

import com.google.gson.annotations.SerializedName

data class SearchResultSetApiObject(
    @SerializedName("results") val results: List<SearchItunesApiDataObject>?
)
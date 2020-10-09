package com.guardian.core.search.api.itunes

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesSearchApi {

    @GET("search")
    fun search(
        @Query("term") searchTerm: String,
        @Query("media") media: String = MEDIA_PODCAST
    ): Single<SearchResultSetApiObject>

    companion object {
        val ENDPOINT = "https://itunes.apple.com/"
        val MEDIA_PODCAST = "podcast"
    }
}

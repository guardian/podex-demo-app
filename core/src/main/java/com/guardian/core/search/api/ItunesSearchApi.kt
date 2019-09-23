package com.guardian.core.search.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ItunesSearchApi {

    @GET( "search")
    fun search (
        @Query("term") searchTerm: String,
        @Path("media") media: String = MEDIA_PODCAST
        ) : Response<SearchResultSetApiObject>


    companion object {
        val ENDPOINT = "https://itunes.apple.com/"
        val MEDIA_PODCAST = "podcast"
    }
}
package com.guardian.core.search

import com.guardian.core.search.api.ItunesSearchApi
import javax.inject.Inject

class SearchRepositoryImpl
@Inject constructor(val itunesSearchApi: ItunesSearchApi)
    : SearchRepository {
    override suspend fun doSearch(term: String): List<SearchResult> {
        return itunesSearchApi.search(term).body()?.results?.map {
            SearchResult(
                it.name,
                it.atworkUrl100String,
                it.feedUrlString
            )
        } ?: listOf()
    }
}
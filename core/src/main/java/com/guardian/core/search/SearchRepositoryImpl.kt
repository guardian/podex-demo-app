package com.guardian.core.search

import com.guardian.core.search.api.ItunesSearchApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class SearchRepositoryImpl
@Inject constructor(private val itunesSearchApi: ItunesSearchApi) :
    SearchRepository {
    override suspend fun doSearch(term: String): List<SearchResult> {
        return withContext(Dispatchers.IO) {
            itunesSearchApi.search(term).awaitResponse().body()?.results?.filter {
                it.feedUrlString != null && it.feedUrlString.isNotEmpty()
                // todo filter invalid feed urls out
            }?.map {
                SearchResult(
                    it.name ?: "",
                    it.atworkUrl600String ?: "",
                    it.feedUrlString ?: ""
                )
            } ?: listOf()
        }
    }
}
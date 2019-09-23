package com.guardian.core.search

import androidx.lifecycle.MutableLiveData
import com.guardian.core.search.api.ItunesSearchApi
import javax.inject.Inject


interface SearchRepository {
    suspend fun doSearch(term: String): List<SearchResult>
}
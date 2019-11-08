package com.guardian.podxdemo.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.guardian.core.search.SearchRepository
import com.guardian.core.search.SearchResult
import javax.inject.Inject

class SearchViewModel
@Inject constructor(searchRepository: SearchRepository)
    : ViewModel() {
    val searchResults: LiveData<List<SearchResult>> = liveData {
        emit(searchRepository.doSearch("news"))
    }
}
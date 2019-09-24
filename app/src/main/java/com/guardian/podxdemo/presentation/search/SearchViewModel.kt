package com.guardian.podxdemo.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.guardian.core.search.SearchRepository
import javax.inject.Inject

class SearchViewModel
@Inject constructor(searchRepository: SearchRepository)
    : ViewModel() {
    val searchResults = liveData {
        emit(searchRepository.doSearch("*"))
    }
}
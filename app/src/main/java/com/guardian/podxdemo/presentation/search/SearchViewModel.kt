package com.guardian.podxdemo.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.core.search.SearchRepository
import com.guardian.core.search.SearchResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel
@Inject constructor(val searchRepository: SearchRepository) :
    ViewModel() {

    val searchResults: MutableLiveData<List<SearchResult>> = MutableLiveData()

    var searchString = "guardian"

    fun doSearch() = viewModelScope.launch {
        searchResults.postValue(searchRepository.doSearch(searchString))
    }
}
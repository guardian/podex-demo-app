package com.guardian.podxdemo.presentation.search

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.guardian.core.search.SearchRepository
import com.guardian.core.search.SearchResult
import kotlinx.coroutines.*
import javax.inject.Inject

class SearchViewModel
@Inject constructor(val searchRepository: SearchRepository)
    : ViewModel() {

    val searchResults: MutableLiveData<List<SearchResult>> = MutableLiveData()

    var searchString = "news"

    fun doSearch () = viewModelScope.launch{
        searchResults.postValue(searchRepository.doSearch(searchString))
    }
}
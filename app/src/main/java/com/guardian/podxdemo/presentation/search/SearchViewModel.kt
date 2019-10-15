package com.guardian.podxdemo.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.core.search.SearchRepository
import com.guardian.core.search.SearchResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel
@Inject constructor(private val searchRepository: SearchRepository) :
    ViewModel() {

    private val searchResults: MutableLiveData<List<SearchResult>> = MutableLiveData()

    val uiModel: SearchUiModel by lazy {
        SearchUiModel(
            searchResults
        )
    }

    fun doSearch(search: String) = viewModelScope.launch {
        searchResults.postValue(searchRepository.doSearch(search))
    }
}

data class SearchUiModel(
    val results: LiveData<List<SearchResult>>
)
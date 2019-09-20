package com.guardian.podcastplayer.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.entities.search.SearchResult

class SearchViewModel: ViewModel() {
    val searchResults: MutableLiveData<List<SearchResult>> by lazy {

    }

    fun newSearch(term: String) {
    }
}
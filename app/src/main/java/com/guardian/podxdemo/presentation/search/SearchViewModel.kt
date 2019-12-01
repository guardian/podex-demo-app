package com.guardian.podxdemo.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.search.SearchRepository
import com.guardian.core.search.SearchResult
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

data class SearchUiModel(
    val results: LiveData<List<SearchResult>>
)

class SearchViewModel
@Inject constructor(private val searchRepository: SearchRepository) :
    ViewModel() {

    val uiModel: SearchUiModel by lazy {
        SearchUiModel(
            searchResults
        )
    }

    private val searchResults: MutableLiveData<List<SearchResult>> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()

    fun doSearch(search: String) {
        compositeDisposable.clear()
        compositeDisposable.add(searchRepository.doSearch(search)
            .subscribe {
                searchResults.postValue(it)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

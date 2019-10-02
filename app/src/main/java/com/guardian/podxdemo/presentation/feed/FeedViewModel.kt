package com.guardian.podxdemo.presentation.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.core.feed.Feed
import com.guardian.core.feed.FeedRepository
import com.guardian.core.search.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import javax.inject.Inject

class FeedViewModel
@Inject constructor(val feedRepository: FeedRepository)
    : ViewModel() {
    val feedData: MutableLiveData<Feed> = MutableLiveData()

    fun setPlaceholderData(searchResult: SearchResult) {
        feedData.postValue(
            Feed(
                feedUrlString = searchResult.feedUrlString,
                title = searchResult.title,
                feedImageUrlString = searchResult.imageUrlString,
                description = "",
                feedItems = listOf()
            )
        )
    }

    fun getFeed(feedUrl: String) = viewModelScope.launch(Dispatchers.IO) {
        feedData.postValue(feedRepository.getFeed(feedUrl))
    }
}
package com.guardian.podxdemo.presentation.feed

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.core.feed.Feed
import com.guardian.core.feed.FeedRepository
import com.guardian.core.search.SearchResult
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FeedViewModel
@Inject constructor(
    val feedRepository: FeedRepository,
    val mediaSessionConnection: MediaSessionConnection
) :
    ViewModel() {
    val feedData: MutableLiveData<Feed> = MutableLiveData(
        Feed("", "", "", "", listOf())
    )

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

    fun getFeed(feedUrl: String) {
        mediaSessionConnection.subscribe(feedUrl, subscriptionCallback)

        viewModelScope.launch {
            feedData.postValue(feedRepository.getFeed(feedUrl))
        }
    }

    object subscriptionCallback : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            children.forEach { Timber.i(it.mediaId) }
            super.onChildrenLoaded(parentId, children)
        }
    }
}
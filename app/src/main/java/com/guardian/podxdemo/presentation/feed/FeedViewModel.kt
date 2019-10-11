package com.guardian.podxdemo.presentation.feed

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardian.core.feed.Feed
import com.guardian.core.feed.FeedRepository
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.FeedItemRepository
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.search.SearchResult
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FeedViewModel
@Inject constructor(
    val feedRepository: FeedRepository,
    val feedItemRepository: FeedItemRepository,
    val mediaSessionConnection: MediaSessionConnection
) :
    ViewModel() {
    val feedData: MutableLiveData<Feed>
        by lazy { MutableLiveData<Feed>() }

    val feedItemData: MutableLiveData<List<FeedItem>>
        by lazy { MutableLiveData<List<FeedItem>>(listOf())}

    fun setPlaceholderData(searchResult: SearchResult) {
        feedData.postValue(
            Feed(
                feedUrlString = searchResult.feedUrlString,
                title = searchResult.title,
                feedImageUrlString = searchResult.imageUrlString,
                description = ""
            )
        )
    }

    fun getFeedAndItems(feedUrl: String) {
        viewModelScope.launch {
            feedRepository.getFeed(feedUrl).observeForever { feed ->
                Timber.i("got feed data changed ${feed?.feedUrlString ?: "null feed"}")
                if (feed != null) {
                    feedData.postValue(feed)
                    getFeedItems(feed)
                }
            }
        }
    }

    private fun getFeedItems(feed: Feed) {
        viewModelScope.launch {
            feedItemRepository.getFeedItemsForFeed(feed).observeForever { feedItemList ->
                Timber.i("list from repo ${feedItemList.size}")
                feedItemData.postValue(feedItemList)
            }
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
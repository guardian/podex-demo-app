package com.guardian.podxdemo.presentation.feed

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.feed.Feed
import com.guardian.core.feed.FeedRepository
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.FeedItemRepository
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.search.SearchResult
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

data class FeedUiModel(
    val feedData: LiveData<Feed>,
    val feedItemData: LiveData<List<FeedItem>>
)

class FeedViewModel
@Inject constructor(
    private val feedRepository: FeedRepository,
    private val feedItemRepository: FeedItemRepository,
    mediaSessionConnection: MediaSessionConnection
) :
    ViewModel() {

    init {
        mediaSessionConnection.subscribe("unused",
            object : MediaBrowserCompat.SubscriptionCallback() {
            }
        )
    }

    val uiModel by lazy {
        FeedUiModel(
            mutableFeedData,
            mutableFeedItemData
        )
    }

    private val mutableFeedData: MutableLiveData<Feed> = MutableLiveData()
    private val mutableFeedItemData: MutableLiveData<List<FeedItem>> = MutableLiveData(listOf())

    private val compositeDisposable = CompositeDisposable()

    fun setPlaceholderData(searchResult: SearchResult) {
        mutableFeedData.postValue(
            Feed(
                feedUrlString = searchResult.feedUrlString,
                title = searchResult.title,
                feedImageUrlString = searchResult.imageUrlString,
                description = ""
            )
        )
    }

    fun getFeedAndItems(feedUrl: String) {
        compositeDisposable.clear()
        compositeDisposable.add(feedRepository.getFeed(feedUrl)
            .subscribe { feed ->
                Timber.i("got feed data changed ${feed?.feedUrlString ?: "null feed"}")
                if (feed != null) {
                    mutableFeedData.postValue(feed)
                    getFeedItems(feed)
                }
            }
        )
    }

    private fun getFeedItems(feed: Feed) {
        compositeDisposable.add(feedItemRepository.getFeedItemsForFeed(feed)
            .subscribe { feedItemList ->
                Timber.i("list from repo ${feedItemList.size}")
                mutableFeedItemData.postValue(feedItemList)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
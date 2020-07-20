package com.guardian.podx.presentation.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.core.feed.Feed
import com.guardian.core.feed.FeedRepository
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.FeedItemRepository
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.id
import com.guardian.core.mediaplayer.extensions.isPlayEnabled
import com.guardian.core.mediaplayer.extensions.isPlaying
import com.guardian.core.mediaplayer.extensions.isPrepared
import com.guardian.core.mediaplayer.podx.PodXEventEmitter
import com.guardian.core.search.SearchResult
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

data class FeedUiModel(
    val feedData: LiveData<Feed>,
    val feedItemData: LiveData<List<FeedItem>>,
    val nowPlayingIdLiveData: LiveData<String?>,
    val isPlayingLiveData: LiveData<Boolean>
)

class FeedViewModel
@Inject constructor(
    private val feedRepository: FeedRepository,
    private val feedItemRepository: FeedItemRepository,
    private val mediaSessionConnection: MediaSessionConnection,
    private val podXEventEmitter: PodXEventEmitter
) : ViewModel() {
    val uiModel by lazy {
        FeedUiModel(
            mutableFeedData,
            mutableFeedItemData,
            mutableNowPlayingIdLiveData,
            mutableIsPlayingLiveData
        )
    }

    private val mutableFeedData: MutableLiveData<Feed> = MutableLiveData()
    private val mutableFeedItemData: MutableLiveData<List<FeedItem>> = MutableLiveData(listOf())
    private val mutableNowPlayingIdLiveData = MutableLiveData<String?>().apply {
        mediaSessionConnection.nowPlaying
            .observeForever {
                this.postValue(it.id)
            }
    }
    private val mutableIsPlayingLiveData = MutableLiveData(false).apply {
        mediaSessionConnection.playbackState.observeForever {
            this.postValue(it.isPlaying)
        }
    }

    private val feedDisposable = CompositeDisposable()
    private val feedItemDisposable = CompositeDisposable()

    fun setPlaceholderData(searchResult: SearchResult) {
        mutableFeedData.postValue(
            Feed(
                feedUrlString = searchResult.feedUrlString,
                title = searchResult.title,
                author = "",
                feedImageUrlString = searchResult.imageUrlString,
                description = ""
            )
        )
    }

    fun getFeedAndItems(feedUrl: String) {
        feedDisposable.clear()
        feedDisposable.add(
            feedRepository.getFeed(feedUrl)
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
        feedItemDisposable.clear()
        feedItemDisposable.add(
            feedItemRepository.getFeedItemsForFeed(feed)
                .subscribe { feedItemList ->
                    Timber.i("list from repo ${feedItemList.size}")
                    mutableFeedItemData.postValue(feedItemList)
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        feedDisposable.clear()
        feedItemDisposable.clear()
    }

    fun prepareFeedItemForPlayback(feedItem: FeedItem) {
        val nowPlaying = mediaSessionConnection.nowPlaying.value
        val transportControls = mediaSessionConnection.transportControls

        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (!(isPrepared && feedItem.feedItemAudioUrl == nowPlaying?.id)) {
            transportControls.prepareFromMediaId(feedItem.feedItemAudioUrl, null)
            podXEventEmitter.registerCurrentFeedItem(feedItem)
        }
    }

    fun attemptPlaybackOrPause(feedItem: FeedItem) {
        val nowPlaying = mediaSessionConnection.nowPlaying.value
        val transportControls = mediaSessionConnection.transportControls

        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (!(isPrepared && feedItem.feedItemAudioUrl == nowPlaying?.id)) {
            transportControls.prepareFromMediaId(feedItem.feedItemAudioUrl, null)
            podXEventEmitter.registerCurrentFeedItem(feedItem)
        } else {
            mediaSessionConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Timber.w(
                            "%s%s", "Playable item clicked but neither play ",
                            "nor pause are enabled!"
                        )
                    }
                }
            }
        }
    }
}

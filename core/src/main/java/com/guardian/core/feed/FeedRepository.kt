package com.guardian.core.feed

import androidx.lifecycle.LiveData

interface FeedRepository {
    suspend fun getFeed(feedUrl: String): LiveData<Feed>
    fun getFeeds(): LiveData<List<Feed>>
}
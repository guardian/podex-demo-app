package com.guardian.core.feed

interface FeedRepository {
    suspend fun getFeed(feedUrl: String): Feed
}
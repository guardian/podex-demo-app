package com.guardian.core.feed

data class Feed (
    val feedUrlString: String,
    val title: String,
    val feedImageUrlString: String,
    val description: String,
    val feedItems: List<FeedItem>
)
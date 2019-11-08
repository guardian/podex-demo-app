package com.guardian.core.feed

data class Feed (
    val title: String,
    val description: String,
    val feedItems: List<FeedItem>
)
package com.guardian.core.feed

import java.util.*

data class FeedItem (
    val title: String,
    val description: String,
    val imageUrlString: String,
    val pubDate: Date
)

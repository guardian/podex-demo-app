package com.guardian.core.feed

import androidx.room.Embedded
import androidx.room.Relation
import com.guardian.core.feeditem.FeedItem

data class FeedWithItems(
    @Relation(
        parentColumn = "feedUrlString",
        entityColumn = "feedUrlString",
        entity = FeedItem::class
    )
    val feedItem: List<FeedItem>,
    @Embedded
    val feed: Feed
)

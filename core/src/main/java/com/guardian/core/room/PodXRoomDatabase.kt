package com.guardian.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.guardian.core.feed.Feed
import com.guardian.core.feed.dao.FeedDao
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.dao.FeedItemDao

@Database(entities = [Feed::class, FeedItem::class], version = PodXRoomDatabase.VERSION)
@TypeConverters(DateTypeConverters::class)
abstract class PodXRoomDatabase : RoomDatabase() {
    abstract fun getFeedDao(): FeedDao
    abstract fun getFeedItemDao(): FeedItemDao

    companion object {
        const val NAME = "podx-data"
        const val VERSION = 1
    }
}
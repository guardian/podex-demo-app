package com.guardian.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.guardian.core.feed.Feed
import com.guardian.core.feed.dao.FeedDao
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.dao.FeedItemDao
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXSupportEvent
import com.guardian.core.podxevent.PodXWebEvent
import com.guardian.core.podxevent.dao.PodXImageEventDao
import com.guardian.core.podxevent.dao.PodXSupportEventDao
import com.guardian.core.podxevent.dao.PodXWebEventDao

@Database(entities = [
    Feed::class,
    FeedItem::class,
    PodXImageEvent::class,
    PodXWebEvent::class,
    PodXSupportEvent::class
],
    version = PodXRoomDatabase.VERSION)
@TypeConverters(RoomTypeConverters::class)
abstract class PodXRoomDatabase : RoomDatabase() {
    abstract fun getFeedDao(): FeedDao
    abstract fun getFeedItemDao(): FeedItemDao
    abstract fun getPodXImageEventDao(): PodXImageEventDao
    abstract fun getPodXWebEventDao(): PodXWebEventDao
    abstract fun getPodXSupportEventDao(): PodXSupportEventDao

    companion object {
        const val NAME = "podx-data"
        const val VERSION = 1
    }
}
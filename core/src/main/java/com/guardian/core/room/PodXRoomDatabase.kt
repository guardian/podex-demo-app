package com.guardian.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.guardian.core.feed.Feed
import com.guardian.core.feed.dao.FeedDao
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.dao.FeedItemDao
import com.guardian.core.podxevent.PodXCallPromptEvent
import com.guardian.core.podxevent.PodXFeedBackEvent
import com.guardian.core.podxevent.PodXFeedLinkEvent
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXNewsLetterSignUpEvent
import com.guardian.core.podxevent.PodXPollEvent
import com.guardian.core.podxevent.PodXSocialPromptEvent
import com.guardian.core.podxevent.PodXSupportEvent
import com.guardian.core.podxevent.PodXTextEvent
import com.guardian.core.podxevent.PodXWebEvent
import com.guardian.core.podxevent.dao.PodXCallPromptEventDao
import com.guardian.core.podxevent.dao.PodXFeedBackEventDao
import com.guardian.core.podxevent.dao.PodXFeedLinkEventDao
import com.guardian.core.podxevent.dao.PodXImageEventDao
import com.guardian.core.podxevent.dao.PodXNewsLetterSignUpEventDao
import com.guardian.core.podxevent.dao.PodXPollEventDao
import com.guardian.core.podxevent.dao.PodXSocialPromptEventDao
import com.guardian.core.podxevent.dao.PodXSupportEventDao
import com.guardian.core.podxevent.dao.PodXTextEventDao
import com.guardian.core.podxevent.dao.PodXWebEventDao

@Database(
    entities = [
        Feed::class,
        FeedItem::class,
        PodXImageEvent::class,
        PodXWebEvent::class,
        PodXSupportEvent::class,
        PodXCallPromptEvent::class,
        PodXFeedBackEvent::class,
        PodXFeedLinkEvent::class,
        PodXNewsLetterSignUpEvent::class,
        PodXPollEvent::class,
        PodXSocialPromptEvent::class,
        PodXTextEvent::class
    ],
    version = PodXRoomDatabase.VERSION
)
@TypeConverters(RoomTypeConverters::class)
abstract class PodXRoomDatabase : RoomDatabase() {
    abstract fun getFeedDao(): FeedDao
    abstract fun getFeedItemDao(): FeedItemDao
    abstract fun getPodXImageEventDao(): PodXImageEventDao
    abstract fun getPodXWebEventDao(): PodXWebEventDao
    abstract fun getPodXSupportEventDao(): PodXSupportEventDao
    abstract fun getPodXCallPromptEventDao(): PodXCallPromptEventDao
    abstract fun getPodXFeedBackEventDao(): PodXFeedBackEventDao
    abstract fun getPodXFeedLinkEventDao(): PodXFeedLinkEventDao
    abstract fun getPodXNewsLetterSignUpEventDao(): PodXNewsLetterSignUpEventDao
    abstract fun getPodXPollEventDao(): PodXPollEventDao
    abstract fun getPodXSocialPromptEventDao(): PodXSocialPromptEventDao
    abstract fun getPodXTextEventDao(): PodXTextEventDao

    companion object {
        const val NAME = "podx-data"
        const val VERSION = 1
    }
}

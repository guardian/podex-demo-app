package com.guardian.core.dagger

import android.content.Context
import androidx.room.Room
import com.guardian.core.feed.dao.FeedDao
import com.guardian.core.feeditem.dao.FeedItemDao
import com.guardian.core.podxevent.dao.PodXImageEventDao
import com.guardian.core.podxevent.dao.PodXSupportEventDao
import com.guardian.core.podxevent.dao.PodXWebEventDao
import com.guardian.core.room.PodXRoomDatabase
import dagger.Module
import dagger.Provides

@Module
class RoomModule {
    @Provides
    fun providePodXRoomDatabase(context: Context): PodXRoomDatabase {
        return Room.databaseBuilder(context, PodXRoomDatabase::class.java, PodXRoomDatabase.NAME)
            .build()
    }

    @Provides
    fun provideFeedDao(podXRoomDatabase: PodXRoomDatabase): FeedDao {
        return podXRoomDatabase.getFeedDao()
    }

    @Provides
    fun provideFeedItemDao(podXRoomDatabase: PodXRoomDatabase): FeedItemDao {
        return podXRoomDatabase.getFeedItemDao()
    }

    @Provides
    fun providePodXImageEventDao(podXRoomDatabase: PodXRoomDatabase): PodXImageEventDao {
        return podXRoomDatabase.getPodXImageEventDao()
    }

    @Provides
    fun providePodXWebEventDao(podXRoomDatabase: PodXRoomDatabase): PodXWebEventDao {
        return podXRoomDatabase.getPodXWebEventDao()
    }

    @Provides
    fun providePodXSupportEventDao(podXRoomDatabase: PodXRoomDatabase): PodXSupportEventDao {
        return podXRoomDatabase.getPodXSupportEventDao()
    }
}
package com.guardian.core.dagger

import android.content.ComponentName
import android.content.Context
import com.guardian.core.mediaplayer.MediaService
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.podx.PodXEventEmitter
import com.guardian.core.mediaplayer.podx.PodXEventEmitterImpl
import com.guardian.core.podxevent.dao.PodXEventDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MediaSessionConnectionModule {
    @Provides
    @Singleton
    fun provideMediaSessionConnection(context: Context): MediaSessionConnection {
        return MediaSessionConnection(
            context,
            ComponentName(context, MediaService::class.java)
        )
    }

    @Provides
    @Singleton
    fun providePodXEventEmitter(
        mediaSessionConnection: MediaSessionConnection,
        podXEventDao: PodXEventDao
    ): PodXEventEmitter {
        return PodXEventEmitterImpl(mediaSessionConnection, podXEventDao)
    }
}
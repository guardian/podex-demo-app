package com.guardian.core.dagger

import android.content.ComponentName
import android.content.Context
import com.guardian.core.mediaplayer.MediaService
import com.guardian.core.mediaplayer.common.MediaSessionConnection
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
}
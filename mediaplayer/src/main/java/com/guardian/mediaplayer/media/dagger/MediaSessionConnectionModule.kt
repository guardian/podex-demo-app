package com.guardian.mediaplayer.media.dagger

import android.content.ComponentName
import android.content.Context
import com.guardian.mediaplayer.media.MusicService
import com.guardian.mediaplayer.media.common.MediaSessionConnection
import dagger.Module
import dagger.Provides

@Module
class MediaSessionConnectionModule {
    @Provides
    fun provideMediaSessionConnection(context: Context): MediaSessionConnection {
        return MediaSessionConnection(context, ComponentName(context, MusicService::class.java))
    }
}
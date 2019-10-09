package com.guardian.core.dagger

import android.content.ComponentName
import android.content.Context
import com.guardian.core.mediaplayer.MediaService
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import dagger.Module
import dagger.Provides

@Module
class MediaSessionConnectionModule {
    @Provides
    fun provideMediaSessionConnection(context: Context): MediaSessionConnection {
        return MediaSessionConnection(
            context,
            ComponentName(context, MediaService::class.java)
        )
    }
}
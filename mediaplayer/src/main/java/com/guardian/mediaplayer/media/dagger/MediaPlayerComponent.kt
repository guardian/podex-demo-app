package com.guardian.mediaplayer.media.dagger

import android.content.Context
import com.guardian.mediaplayer.media.common.MediaSessionConnection
import dagger.BindsInstance
import dagger.Component

@Component(modules = [MediaSessionConnectionModule::class])
interface MediaPlayerComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindContext(context: Context): Builder
        fun build(): MediaPlayerComponent
    }

    fun provideMediaSessionConnection(): MediaSessionConnection
}
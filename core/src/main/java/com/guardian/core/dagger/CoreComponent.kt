package com.guardian.core.dagger

import android.content.Context
import com.guardian.core.dagger.search.SearchDataModule
import com.guardian.core.feed.FeedRepository
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.search.SearchRepository
import dagger.BindsInstance
import dagger.Component

/**
 * Component providing application wide singletons.
 */
@Component(modules = [WebModule::class,
    SearchDataModule::class,
    RepositoryModule::class,
    MediaSessionConnectionModule::class,
    RoomModule::class])
interface CoreComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindContext(context: Context): Builder
        fun build(): CoreComponent
    }

    fun provideSearchRepository(): SearchRepository
    fun provideFeedRepository(): FeedRepository
    fun provideMediaSessionConnection(): MediaSessionConnection
}

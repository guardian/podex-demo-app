package com.guardian.core.dagger

import android.content.Context
import com.guardian.core.feed.FeedRepository
import com.guardian.core.feeditem.FeedItemRepository
import com.guardian.core.mediametadata.MediaMetadataRepository
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.podxevent.PodXEventRepository
import com.guardian.core.search.SearchRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing application wide singletons.
 */
@Singleton
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
    fun provideFeedItemRepository(): FeedItemRepository
    fun providePodXEventRepository(): PodXEventRepository
    fun provideMediaMetadataRepository(): MediaMetadataRepository
    fun provideMediaSessionConnection(): MediaSessionConnection
}

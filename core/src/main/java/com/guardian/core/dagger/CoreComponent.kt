package com.guardian.core.dagger

import com.guardian.core.dagger.search.SearchDataModule
import com.guardian.core.feed.FeedRepository
import com.guardian.core.search.SearchRepository
import dagger.Component

/**
 * Component providing application wide singletons.
 */
@Component(modules = [WebModule::class,
    SearchDataModule::class,
    RepositoryModule::class])
interface CoreComponent {

    @Component.Builder
    interface Builder {
        fun build(): CoreComponent
    }

    fun provideSearchRepository(): SearchRepository
    fun provideFeedRepository(): FeedRepository
}

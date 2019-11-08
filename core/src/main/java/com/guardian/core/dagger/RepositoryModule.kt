package com.guardian.core.dagger

import com.guardian.core.feed.FeedRepository
import com.guardian.core.feed.FeedRepositoryImpl
import com.guardian.core.feed.api.GeneralFeedApi
import com.guardian.core.feed.api.GeneralFeedApiImpl
import com.guardian.core.feeditem.FeedItemRepository
import com.guardian.core.feeditem.FeedItemRepositoryImpl
import com.guardian.core.mediametadata.MediaMetadataRepository
import com.guardian.core.mediametadata.MediaMetadataRepositoryImpl
import com.guardian.core.search.SearchRepository
import com.guardian.core.search.SearchRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository

    @Binds
    abstract fun bindFeedRepository(feedRepositoryImpl: FeedRepositoryImpl): FeedRepository

    @Binds
    abstract fun bindFeedItemRepository(feedItemRepositoryImpl: FeedItemRepositoryImpl):
        FeedItemRepository

    @Binds
    abstract fun bindGeneralFeedApi(generalFeedApiImpl: GeneralFeedApiImpl): GeneralFeedApi

    @Binds
    abstract fun bindMediaMetadataRepository(
        mediaMetadataRepositoryImpl: MediaMetadataRepositoryImpl
    ): MediaMetadataRepository
}

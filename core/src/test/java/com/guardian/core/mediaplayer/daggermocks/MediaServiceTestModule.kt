package com.guardian.core.mediaplayer.daggermocks

import com.guardian.core.feed.FeedWithItems
import com.guardian.core.feed.dao.FeedDao
import com.guardian.core.mediametadata.MediaMetadataRepository
import com.guardian.core.mediametadata.MediaMetadataRepositoryImpl
import com.guardian.core.mediaplayer.MediaService
import com.guardian.core.mediaplayer.PackageValidator
import com.guardian.core.testutils.MockedFeedDataSources
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.Flowable
import org.mockito.Mockito

@Module
abstract class AbstractMediaServiceTestModule {
    @ContributesAndroidInjector
    abstract fun mediaService(): MediaService
}

@Module
class MediaServiceTestModule {
    @Provides
    fun providePackageValidator(): PackageValidator = TestPackageValidator()

    @Provides
    fun provideMockedMediaMetadataRepository(): MediaMetadataRepository {
        val feedDao: FeedDao = Mockito.mock(FeedDao::class.java).also {
                Mockito.`when`(it.getCachedFeedsWithFeedItems())
                    .then {
                        Flowable
                            .just<List<FeedWithItems>>(
                                listOf(FeedWithItems(listOf(MockedFeedDataSources.testFeedItem1, MockedFeedDataSources.testFeedItem2), MockedFeedDataSources.testFeed))
                            )
                    }
            }

        return MediaMetadataRepositoryImpl(feedDao, MockedFeedDataSources.feedRepository, MockedFeedDataSources.feedItemRepository)
    }
}
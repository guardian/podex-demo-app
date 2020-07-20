package com.guardian.core.coretestapplication

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.FileDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.guardian.core.feed.FeedWithItems
import com.guardian.core.feed.dao.FeedDao
import com.guardian.core.mediametadata.MediaMetadataRepository
import com.guardian.core.mediametadata.MediaMetadataRepositoryImpl
import com.guardian.core.mediaplayer.MediaService
import com.guardian.core.mediaplayer.PackageValidator
import com.guardian.core.mediaplayer.library.FeedSource
import com.guardian.core.testutils.InstrumentationMockedFeedDataSources
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.Flowable
import org.mockito.Mockito
import timber.log.Timber

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
                            listOf(
                                FeedWithItems(
                                    InstrumentationMockedFeedDataSources.testFeedList,
                                    InstrumentationMockedFeedDataSources.testFeed
                                )
                            )
                        )
                }
        }

        return MediaMetadataRepositoryImpl(
            feedDao, InstrumentationMockedFeedDataSources.feedRepository,
            InstrumentationMockedFeedDataSources.feedItemRepository
        )
    }

    @Provides
    fun provideExoplayer(context: Context): ExoPlayer {
        // todo mock exoplayer
        val uAmpAudioAttributes = AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        return ExoPlayerFactory.newSimpleInstance(context).apply {
            setAudioAttributes(uAmpAudioAttributes, true)
            playWhenReady = true
        }
    }

    @Provides
    fun provideDataSourceFactory(context: Context): DataSource.Factory = FileDataSourceFactory(
        object : TransferListener {
            override fun onTransferInitializing(
                source: DataSource?,
                dataSpec: DataSpec?,
                isNetwork: Boolean
            ) {
                if (isNetwork) {
                    throw IllegalAccessException("Unit test Attempting network access")
                }
                Timber.i("Initialising from ${source?.uri}")
            }

            override fun onTransferStart(
                source: DataSource?,
                dataSpec: DataSpec?,
                isNetwork: Boolean
            ) {
                if (isNetwork) {
                    throw IllegalAccessException("Unit test Attempting network access")
                }
                Timber.i("Starting from ${source?.uri}")
            }

            override fun onTransferEnd(
                source: DataSource?,
                dataSpec: DataSpec?,
                isNetwork: Boolean
            ) {
                if (isNetwork) {
                    throw IllegalAccessException("Unit test Used network access")
                }
                Timber.i("Finished transfer from ${source?.uri}")
            }

            override fun onBytesTransferred(
                source: DataSource?,
                dataSpec: DataSpec?,
                isNetwork: Boolean,
                bytesTransferred: Int
            ) {
                if (isNetwork) {
                    throw IllegalAccessException("Unit test Attempting network access")
                }
                Timber.i("Finished transfer of ${bytesTransferred}B from ${source?.uri}")
            }
        }
    )

    @Provides
    fun provideFeedSource(mediaMetadataRepository: MediaMetadataRepository): FeedSource {
        // bind test feed source instance to graph
        return FeedSource(mediaMetadataRepository)
    }
}

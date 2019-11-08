package com.guardian.core.mediametadata

import android.os.Build
import com.guardian.core.feed.FeedWithItems
import com.guardian.core.feed.dao.FeedDao
import com.guardian.core.mediaplayer.extensions.album
import com.guardian.core.mediaplayer.extensions.albumArtUri
import com.guardian.core.mediaplayer.extensions.artist
import com.guardian.core.mediaplayer.extensions.displayDescription
import com.guardian.core.mediaplayer.extensions.displayIconUri
import com.guardian.core.mediaplayer.extensions.displaySubtitle
import com.guardian.core.mediaplayer.extensions.displayTitle
import com.guardian.core.mediaplayer.extensions.duration
import com.guardian.core.mediaplayer.extensions.id
import com.guardian.core.mediaplayer.extensions.mediaUri
import com.guardian.core.mediaplayer.extensions.title
import com.guardian.core.mediaplayer.extensions.trackCount
import com.guardian.core.mediaplayer.extensions.trackNumber
import com.guardian.core.testutils.MockedFeedDataSources
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import timber.log.Timber

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class MediaMetadataRepositoryTest {

    private val feedDao: FeedDao by lazy {
        Mockito.mock(FeedDao::class.java).also {
            Mockito.`when`(it.getCachedFeedsWithFeedItems())
                .then {
                    Flowable
                        .just<List<FeedWithItems>>(
                            listOf(FeedWithItems(listOf(MockedFeedDataSources.testFeedItem1, MockedFeedDataSources.testFeedItem2), MockedFeedDataSources.testFeed))
                        )
                }
        }
    }

    @Before
    fun debugLogger() {
        Timber.plant(object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                println("Unit Test: $message")
                t?.printStackTrace()
            }
        })
    }

    @After
    fun debugLoggerLogger() {
        Timber.uprootAll()
    }


    @Test
    fun getAllMetadata() {
        val mediaMetadataRepository = MediaMetadataRepositoryImpl(feedDao, MockedFeedDataSources.feedRepository, MockedFeedDataSources.feedItemRepository)

        mediaMetadataRepository.getStoredMetadata()
            .observeOn(Schedulers.trampoline())
            .subscribe (
                {metadataList ->
                    assertNotNull(metadataList.find {
                        it.id == MockedFeedDataSources.testFeedItem1.feedItemAudioUrl
                            && it.title ==  MockedFeedDataSources.testFeedItem1.title
                            && it.artist == MockedFeedDataSources.testFeedItem1.author
                            && it.album == MockedFeedDataSources.testFeed.title
                            && it.duration == MockedFeedDataSources.testFeedItem1.lengthMs
                            && it.mediaUri.toString()  == MockedFeedDataSources.testFeedItem1.feedItemAudioUrl
                            && it.albumArtUri.toString()  == MockedFeedDataSources.testFeedItem1.imageUrlString
                            && it.trackNumber == MockedFeedDataSources.testFeedItem1.episodeNumber
                            && it.trackCount == 2L
                            && it.displayTitle == MockedFeedDataSources.testFeedItem1.title
                            && it.displaySubtitle == MockedFeedDataSources.testFeedItem1.author
                            && it.displayDescription == MockedFeedDataSources.testFeedItem1.description
                            && it.displayIconUri.toString() == MockedFeedDataSources.testFeedItem1.imageUrlString
                    })

                    assertNotNull(metadataList.find {
                        it.id == MockedFeedDataSources.testFeedItem2.feedItemAudioUrl
                            && it.title ==  MockedFeedDataSources.testFeedItem2.title
                            && it.artist == MockedFeedDataSources.testFeedItem2.author
                            && it.album == MockedFeedDataSources.testFeed.title
                            && it.duration == MockedFeedDataSources.testFeedItem2.lengthMs
                            && it.mediaUri.toString()  == MockedFeedDataSources.testFeedItem2.feedItemAudioUrl
                            && it.albumArtUri.toString()  == MockedFeedDataSources.testFeedItem2.imageUrlString
                            && it.trackNumber == MockedFeedDataSources.testFeedItem2.episodeNumber
                            && it.trackCount == 2L
                            && it.displayTitle == MockedFeedDataSources.testFeedItem2.title
                            && it.displaySubtitle == MockedFeedDataSources.testFeedItem2.author
                            && it.displayDescription == MockedFeedDataSources.testFeedItem2.description
                            && it.displayIconUri.toString() == MockedFeedDataSources.testFeedItem2.imageUrlString
                    })

                },
                {e -> Timber.e(e)}
            )
    }
}
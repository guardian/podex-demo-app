package com.guardian.core.testutils

import com.guardian.core.podxevent.OGMetadata
import com.guardian.core.podxevent.PodXEventRepository
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXSupportEvent
import com.guardian.core.podxevent.PodXWebEvent
import io.reactivex.Flowable
import org.mockito.Mockito

object InstrumentationMockedEventDataSources {
    val testFeedItem1Image1 = PodXImageEvent(
        timeStart = 3000L,
        timeEnd = 13000L,
        urlString = "http://localhost/testImage11.png",
        caption = "feed 1 image item 1 caption",
        notification = "feed 1 image item 1 caption",
        feedItemUrlString = InstrumentationMockedFeedDataSources.testFeedItem1.feedItemAudioUrl
    )
    val testFeedItem1Image2 = PodXImageEvent(
        timeStart = 7000L,
        timeEnd = 17000L,
        urlString = "http://localhost/testImage12.png",
        caption = "feed 1 image item 2 caption",
        notification = "feed 1 image item 2 caption",
        feedItemUrlString = InstrumentationMockedFeedDataSources.testFeedItem1.feedItemAudioUrl
    )

    val testFeedItem1ImageList = listOf(testFeedItem1Image1, testFeedItem1Image2)

    val testFeedItem1Web1 = PodXWebEvent(
        timeStart = 3000L,
        timeEnd = 13000L,
        urlString = "http://localhost/testWeb11",
        caption = "feed 1 web item 1 caption",
        notification = "feed 1 web item 1 caption",
        feedItemUrlString = InstrumentationMockedFeedDataSources.testFeedItem1.feedItemAudioUrl,
        ogMetadata = OGMetadata(
            "http://localhost/testWeb11/ogimage.png",
            "Feed 1 Web 1",
            "http://localhost/testWeb11/ogurl",
            ""
        )
    )
    val testFeedItem1Web2 = PodXWebEvent(
        timeStart = 7000L,
        timeEnd = 17000L,
        urlString = "http://localhost/testWeb12",
        caption = "feed 1 web item 2 caption",
        notification = "feed 1 web item 2 caption",
        feedItemUrlString = InstrumentationMockedFeedDataSources.testFeedItem1.feedItemAudioUrl,
        ogMetadata = OGMetadata(
            "http://localhost/testWeb12/ogimage.png",
            "Feed 1 Web 2",
            "http://localhost/testWeb12/ogurl",
            ""
        )
    )

    val testFeedItem1WebList = listOf(testFeedItem1Web1, testFeedItem1Web2)

    val podXEventRepository: PodXEventRepository = Mockito.mock(PodXEventRepository::class.java).also { mockedEventRepo ->
        // mock all gets to run in Event emitter Implementation
        Mockito.`when`(
            mockedEventRepo
                .getImageEventsForFeedItem(InstrumentationMockedFeedDataSources.testFeedItem1)
        )
            .then { Flowable.just(testFeedItem1ImageList) }

        Mockito.`when`(
            mockedEventRepo
                .getWebEventsForFeedItem(InstrumentationMockedFeedDataSources.testFeedItem1)
        )
            .then { Flowable.just(testFeedItem1WebList) }

        Mockito.`when`(
            mockedEventRepo
                .getSupportEventsForFeedItem(InstrumentationMockedFeedDataSources.testFeedItem1)
        )
            .then { Flowable.just(listOf<PodXSupportEvent>()) }
    }
}

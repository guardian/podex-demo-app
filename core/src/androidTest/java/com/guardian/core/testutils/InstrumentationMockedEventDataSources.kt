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
        urlString = "https://media-imgops.gutools.co.uk/d/3/9/8/d/b/d398dbc6113ff68132cfa193f9534cbbcef4dfa9?response-content-disposition=attachment%3B%20filename%3D%22%5BB%40479deee2%22%3B%20filename%2A%3DUTF-8%27%27mag%2520mur%2520%2528d398dbc6113ff68132cfa193f9534cbbcef4dfa9%2529.jpg&x-amz-security-token=IQoJb3JpZ2luX2VjEKD%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaCWV1LXdlc3QtMSJGMEQCIEdsEZt9AQyNrKqBQjteZLvgjFIV4FoJ5zPtYE4XmoPvAiBJQs%2B7I7xgA24VF%2FzjXnFb5YC0InHnDXhSYt30twsF2yq9AwjJ%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F8BEAAaDDU2MzU2MzYxMDMxMCIMQMHE5C%2BKOZZmtyhRKpEDkCwQvPscMM2Qu99uTaFy8a6fkf2%2F%2BqQp6Sbd%2B%2FRm8pTHiOWy94U7%2Bw4vZq1ap1amtvw7bqyIBjVt3uTPv4hmT0BwFdJCfzMt1EegP5SO%2BYrMwbp9l0pIN3l52LlLaNrcaAkuaLJbHbSaVT5%2BjBs8qcCr6Q4oxm9AbjyWYdIpORviu0q%2Bq6B2aq3W8x6%2BXLeVCLZQEa1lKyzyKeTTD6jCglw1MmAW0tPz70cMPWbFMvaesKdOeM1HviiY0w3bhVNrrmCEZ6q%2BRErXg073KR2u6owgfkCOHp%2BLdcpqOfqgLa6FWtZgmg0lDCb1lWHCxME5FjhhGQ2cNFlGDEd%2BUFRNRfrSUd2qmi5gMgfP8xjUSgyUojMRI%2FAfm7f0BwrBOQOjtbjq55HtLPUXwgEDPiwm5EEgg6ioO4rwZT4Hggy9vrjlfsSVr2Rt%2FDsLBaCtYCT0pA5%2FKkT3UzQjRBgHhrkSylgs0cu7shSGb2yqA83M%2B%2Fyj%2BYFxChrYECQiPtGRUbBNd8%2B%2BQhjLn2VZDjgD8rsO3nQwoLzp%2BwU67AEkKUtwivgRiN3XzsE3KONK9Kj4wkaaJSG7Q4sViax0zCUAh%2FK4iHToskpnSJT0JEl9lnyom6%2Boya%2BLkBN4h96tqZZrpVI0Hirz5zYcp%2BpJwzrjDOJi52Vh1gZ5HKYhDUm6KbbtbCaJgtohl3p7ZSNE47zxLbm%2BRtcRLJ41ysMKXRzAV7dCAhzn3lZV0Uu1BsilatlVM%2FF8Z05uIsSoXfl%2FZXxhCPBzbiUlk%2BRDrNAwF%2F3%2BRDGfwvFP5VCXFHk%2Bm7Ri8piNl5zCVTFR0EiQKlsu10B%2BcKsyCDegabPF9M9G3Qu7ceq7M8KHWhtUIA%3D%3D&AWSAccessKeyId=ASIAYGNYCIDDBVCFP3PM&Expires=1601860800&Signature=0rJ%2FiOmzzTcm3xd8PkBXwyOJh6c%3D&w=1680&h=1050&q=85",
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

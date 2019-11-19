package com.guardian.core.testutils

import com.guardian.core.feed.Feed
import com.guardian.core.feed.FeedRepository
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.FeedItemRepository
import io.reactivex.Flowable
import org.mockito.Mockito
import java.time.Instant
import java.util.Date

object InstrumentationMockedFeedDataSources {

    val testFeed: Feed = Feed(
        feedUrlString = "https://www.theguardian.com/australia-news/series/australian-politics-live",
        title = "PodX Test Feed",
        feedImageUrlString = "https://uploads.guim.co.uk/2019/06/03/Australian_Politics_Live_3000x3000.jpg",
        description = "This is a test feed using audio where Katharine Murphy and Guardian Australia's political team examine what’s happening in Australian politics and why it matters to you. There is additional data to show information about Mark Zuckerburg's dog while the episode plays back."
    )

    val testFeedItem1: FeedItem = FeedItem(
        title = "A whole lot of images",
        description = "The shadow climate minister, Mark Butler, sits down with Katharine Murphy to discuss Labor’s ambitious climate policies. Did they lead to Labor’s election loss? And will Labor’s stance on climate change remain ‘unshakeable’?• Labor’s climate policies are ‘unshakeable’ despite election loss, Mark Butler says. There is a picture of a Pulli dog, and a link to an article about a famous owner.",
        imageUrlString = "https://interactive.guim.co.uk/podx/Puli_600.jpg",
        feedUrlString = "https://interactive.guim.co.uk/podx/podcast.xml",
        feedItemAudioUrl = "https://flex.acast.com/audio.guim.co.uk/2019/09/19-03050-APLMarkButler.mp3",
        feedItemAudioEncoding = "audio/mpeg",
        pubDate = Date.from(Instant.now()),
        author = "The Guardian",
        lengthMs = 1945000,
        episodeNumber = 1
    )

    val testFeedItem2: FeedItem = FeedItem(
        title = "Actually an episode from the science podcast – Australian politics live podcast",
        description = "Prof Stuart Russell wrote the book on artificial intelligence. Literally. But that was back in 1995, when the next few decades of AI were uncertain, and, according to him, distinctly less threatening. Sitting down with Ian Sample, Russell talks about his latest book, Human Compatible, which warns of a dystopian future in which humans are outsmarted by machines. But how did we get here? And what can we do to make sure these machines benefit humankind?\n",
        imageUrlString = "https://interactive.guim.co.uk/podx/Puli_600.jpg",
        feedUrlString = "https://interactive.guim.co.uk/podx/podcast.xml",
        feedItemAudioUrl = "https://flex.acast.com/audio.guim.co.uk/2019/10/17-63694-gnl.sci.20191018.ms.stuart_russel_superintelligent_ai.mp3",
        feedItemAudioEncoding = "audio/mpeg",
        pubDate = Date.from(Instant.now()),
        author = "The Guardian",
        lengthMs = 36459909,
        episodeNumber = 2
    )

    val testFeedList = listOf(testFeedItem1, testFeedItem2)

    val feedRepository: FeedRepository = Mockito.mock(FeedRepository::class.java).also {
            Mockito.`when`(it.getFeed(testFeed.feedUrlString)).then {
                Flowable.just(testFeed)
            }
            Mockito.`when`(it.getFeeds())
                .then { Flowable.just(listOf(testFeed)) }
        }

    val feedItemRepository: FeedItemRepository = Mockito.mock(FeedItemRepository::class.java).also {
            Mockito.`when`(it.getFeedItemsForFeed(testFeed))
                .then { Flowable.just(
                    testFeedItem1,
                    testFeedItem2
                ) }

            Mockito.`when`(it.getFeedItemForUrlString(testFeedItem1.feedItemAudioUrl))
                .then { Flowable.just(testFeedItem1) }

            Mockito.`when`(it.getFeedItemForUrlString(testFeedItem2.feedItemAudioUrl))
                .then { Flowable.just(testFeedItem2) }
        }
}
package com.guardian.core.search.api.spoofedtestfeed

import com.guardian.core.search.SearchResult

// TODO Delet this
class SpoofedTestFeedApiImpl : SpoofedTestFeedApi {
    override fun search(): List<SearchResult> = listOf(
        SearchResult(
            "PodX Test Feed",
            "https://uploads.guim.co.uk/2020/05/07/PodcastArt_FullStory_(2).jpg",
            "https://gdn-cdn.s3.amazonaws.com/firehose/podxtest.xml"
        )
        // ,
        // SearchResult(
        //     "old podx test feed",
        //     "https://uploads.guim.co.uk/2019/06/03/Australian_Politics_Live_3000x3000.jpg",
        //     "https://interactive.guim.co.uk/podx/podcast.xml"
        // )
    )
}

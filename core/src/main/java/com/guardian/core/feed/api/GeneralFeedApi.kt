package com.guardian.core.feed.api

interface GeneralFeedApi {
    suspend fun getFeedDeSerializedXml(feedUrlString: String): FeedXmlDataObject
}

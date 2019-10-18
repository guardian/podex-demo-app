package com.guardian.core.feed.api

import androidx.annotation.WorkerThread

interface GeneralFeedApi {
    /**
     * Resolve an RSS 2.0 feed with the itunes and podx namespaces to the internal
     * [FeedXmlDataObject]. Do not run on the main thread.
     */
    @WorkerThread
    fun getFeedDeSerializedXml(feedUrlString: String): FeedXmlDataObject
}

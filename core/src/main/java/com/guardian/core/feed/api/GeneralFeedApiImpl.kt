package com.guardian.core.feed.api

import com.guardian.core.lib.XmlPullParserAdapter
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.IOException
import javax.inject.Inject

class GeneralFeedApiImpl
@Inject constructor(val xmlPullParserAdapter: XmlPullParserAdapter,
                    val okHttpClient: OkHttpClient)
    : GeneralFeedApi {

    @Throws(IOException::class)
    override suspend fun getFeedDeSerializedXml(feedUrlString: String): FeedXmlDataObject {

        val xmlRequest = Request.Builder()
            .url(feedUrlString)
            .addHeader("Accept", "application/xhtml+xml")
            .addHeader("Accept", "application/rss+xml")
            .addHeader("Accept", "application/xml;q=0.9")
            .build()

        okHttpClient.newCall(xmlRequest).execute().use {
            if (it.isSuccessful) {
                val body = it.body
                if (body != null) {
                    val xmlDataObject = xmlPullParserAdapter
                        .deSerialiseXml(body.byteStream()) { RootXmlDataObject() }
                    if(xmlDataObject is RootXmlDataObject) {
                        //todo merge feed lists

                        return xmlDataObject.rssRoot.feeds.first()
                    }
                }
            }

            throw IOException("Http request Failed: ${it.code}")
        }
    }

}
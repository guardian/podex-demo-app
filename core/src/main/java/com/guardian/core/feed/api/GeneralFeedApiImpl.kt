package com.guardian.core.feed.api

import com.guardian.core.feed.api.xmldataobjects.FeedXmlDataObject
import com.guardian.core.feed.api.xmldataobjects.RootXmlDataObject
import com.guardian.core.library.xml.XmlPullParserAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

class GeneralFeedApiImpl
@Inject constructor(
    private val xmlPullParserAdapter: XmlPullParserAdapter,
    private val okHttpClient: OkHttpClient
) :
    GeneralFeedApi {

    @Throws(IOException::class)
    override fun getFeedDeSerializedXml(feedUrlString: String): FeedXmlDataObject {

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
                    // todo add atom support
                    val xmlDataObject = xmlPullParserAdapter
                        .deSerialiseXml(body.byteStream()) { RootXmlDataObject() }
                    if (xmlDataObject is RootXmlDataObject) {
                        // todo merge feed lists

                        return xmlDataObject.rssRoot.feeds.first()
                    }
                }
            }

            throw IOException("Http request Failed: ${it.code}")
        }
    }
}

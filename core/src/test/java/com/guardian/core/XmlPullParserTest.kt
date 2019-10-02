package com.guardian.core

import com.guardian.core.feed.api.RootXmlDataObject
import com.guardian.core.lib.XmlPullParserAdapterImpl
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.xmlpull.v1.XmlPullParserFactory
import timber.log.Timber

class XmlPullParserTest {

    @Test
    fun testPullParserOnSample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("sampleFeedPodex.xml")

        Timber.plant(object: Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                println("$tag: $message")
                t?.printStackTrace()
            }
        })

        runBlocking {
            val testStream = XmlPullParserAdapterImpl(xmlPullParserFactory = XmlPullParserFactory.newInstance())
                .deSerialiseXml(inputStream) { RootXmlDataObject() } as RootXmlDataObject

            println(testStream)
            println(testStream.rssRoot.feeds.map { channel -> channel.itunesImage.attributes.map { it.value.value } })
        }
    }
}
package com.guardian.core.feed.api

import com.guardian.core.dagger.xml.XmlPullParserAdapterImpl
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.xmlpull.v1.XmlPullParserFactory
import timber.log.Timber

class XmlPullParserTest {
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
    fun testPullParserOnPodXSample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("sample_feed_podex.xml")

        runBlocking {
            val testStream = XmlPullParserAdapterImpl(
                xmlPullParserFactory = XmlPullParserFactory.newInstance()
            )
                .deSerialiseXml(inputStream) { RootXmlDataObject() } as RootXmlDataObject

            println(testStream)
            println(testStream.rssRoot.feeds.map { channel -> channel.itunesImage.attributes.map { it.value.value } })
        }
    }

    @Test
    fun testPullParserOnItunesXmlSample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("sample_feed_itunes.xml")

        runBlocking {
            val testStream = XmlPullParserAdapterImpl(
                xmlPullParserFactory = XmlPullParserFactory.newInstance()
            )
                .deSerialiseXml(inputStream) { RootXmlDataObject() } as RootXmlDataObject

            println(testStream)
            println(testStream.rssRoot.feeds.map { channel -> channel.itunesImage.attributes.map { it.value.value } })
        }
    }
}
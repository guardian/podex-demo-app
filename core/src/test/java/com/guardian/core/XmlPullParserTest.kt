package com.guardian.core

import com.guardian.core.feed.Feed
import com.guardian.core.lib.XmlPullParserAdapterImpl
import org.junit.Test
import org.xmlpull.v1.XmlPullParserFactory

class XmlPullParserTest {

    @Test
    fun testPullParserOnSample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("sampleFeedPodex.xml")

        XmlPullParserAdapterImpl<Feed>(xmlPullParserFactory = XmlPullParserFactory.newInstance())
            .parseXml(inputStream)
    }
}
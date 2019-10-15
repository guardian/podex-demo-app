package com.guardian.core.feed.api

import com.guardian.core.dagger.xml.ValueContainer
import com.guardian.core.dagger.xml.XmlDataObject
import com.guardian.core.dagger.xml.XmlDataObjectFactory

data class FeedItemXmlDataObject(
    val title: String = "",
    val description: String = "",
    val itunesImage: FeedItunesImageXmlDataObject = FeedItunesImageXmlDataObject(),
    val image: FeedImageXmlDataObject = FeedImageXmlDataObject(),
    val pubDate: String = "",
    val enclosureXmlDataObject: FeedItemEnclosureXmlDataObject = FeedItemEnclosureXmlDataObject(),
    val author: String = "",
    val keywords: String = ""
) : XmlDataObject {
    override fun isEmpty(): Boolean = enclosureXmlDataObject.isEmpty()

    override val attributes: Map<String, ValueContainer<String>> = mapOf()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mutableMapOf(
                "title" to ValueContainer(""),
                "description" to ValueContainer(""),
                "itunes:image" to ValueContainer(FeedItunesImageXmlDataObject()),
                "image" to ValueContainer(FeedImageXmlDataObject()),
                "pubDate" to ValueContainer(""),
                "enclosure" to ValueContainer(FeedItemEnclosureXmlDataObject()),
                "itunes:author" to ValueContainer(""),
                "itunes:keywords" to ValueContainer("")
            )
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return FeedItemXmlDataObject(
                title = xmlParserElementMap["title"]?.value as String,
                description = xmlParserElementMap["description"]?.value as String,
                itunesImage = xmlParserElementMap["itunes:image"]?.value
                    as FeedItunesImageXmlDataObject,
                image = xmlParserElementMap["image"]?.value as FeedImageXmlDataObject,
                pubDate = xmlParserElementMap["pubDate"]?.value as String,
                enclosureXmlDataObject = xmlParserElementMap["enclosure"]?.value
                    as FeedItemEnclosureXmlDataObject,
                author = xmlParserElementMap["itunes:author"]?.value as String,
                keywords = xmlParserElementMap["itunes:keywords"]?.value as String
            )
        }
    }
}
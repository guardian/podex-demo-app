package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

data class FeedItemXmlDataObject(
    val title: String = "",
    val description: String = "",
    val itunesImage: FeedItunesImageXmlDataObject = FeedItunesImageXmlDataObject(),
    val image: FeedImageXmlDataObject = FeedImageXmlDataObject(),
    val pubDate: String = "",
    val enclosureXmlDataObject: FeedItemEnclosureXmlDataObject = FeedItemEnclosureXmlDataObject(),
    val duration: String = "",
    val author: String = "",
    val keywords: String = "",
    val podxImages: List<PodxEventXmlDataObject> = listOf(PodxEventXmlDataObject())
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
                "itunes:duration" to ValueContainer(""),
                "itunes:author" to ValueContainer(""),
                "itunes:keywords" to ValueContainer(""),
                "podx:image" to ValueContainer(listOf(PodxEventXmlDataObject()))
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
                duration = xmlParserElementMap["itunes:duration"]?.value as String,
                author = xmlParserElementMap["itunes:author"]?.value as String,
                keywords = xmlParserElementMap["itunes:keywords"]?.value as String,
                podxImages = xmlParserElementMap["podx:image"]?.value as List<PodxEventXmlDataObject>
            )
        }
    }
}
package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

data class FeedXmlDataObject(
    val title: String = "",
    val link: String = "",
    val description: String = "",
    val image: FeedImageXmlDataObject = FeedImageXmlDataObject(),
    val itunesImage: FeedItunesImageXmlDataObject = FeedItunesImageXmlDataObject(),
    val feedItems: List<FeedItemXmlDataObject> = listOf(
        FeedItemXmlDataObject()
    ),
    val author: String = "",
    val owner: FeedItunesOwnerXmlDataObject = FeedItunesOwnerXmlDataObject()
) : XmlDataObject {
    override fun isEmpty(): Boolean = title.isEmpty() &&
            link.isEmpty() &&
            description.isEmpty() &&
            image.isEmpty() &&
            itunesImage.isEmpty() &&
            feedItems.isEmpty() || feedItems.map { it.isEmpty() }.reduce { acc, b -> acc || b }

    override val attributes: Map<String, ValueContainer<String>> = mapOf()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mutableMapOf(
                "title" to ValueContainer(""),
                "link" to ValueContainer(""),
                "description" to ValueContainer(""),
                "image" to ValueContainer(FeedImageXmlDataObject()),
                "itunes:image" to ValueContainer(
                    FeedItunesImageXmlDataObject()
                ),
                "item" to ValueContainer(listOf(FeedItemXmlDataObject())),
                "itunes:author" to ValueContainer(""),
                "itunes:owner" to ValueContainer(FeedItunesOwnerXmlDataObject())
            )
        }

        @Suppress("UNCHECKED_CAST")
        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return FeedXmlDataObject(
                xmlParserElementMap["title"]?.value as String,
                xmlParserElementMap["link"]?.value as String,
                xmlParserElementMap["description"]?.value as String,
                xmlParserElementMap["image"]?.value as FeedImageXmlDataObject,
                xmlParserElementMap["itunes:image"]?.value as FeedItunesImageXmlDataObject,
                xmlParserElementMap["item"]?.value as List<FeedItemXmlDataObject>,
                xmlParserElementMap["itunes:author"]?.value as String,
                xmlParserElementMap["itunes:owner"]?.value as FeedItunesOwnerXmlDataObject
            )
        }
    }
}
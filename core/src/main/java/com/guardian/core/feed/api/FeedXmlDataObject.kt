package com.guardian.core.feed.api

import com.guardian.core.dagger.xml.ValueContainer
import com.guardian.core.dagger.xml.XmlDataObject
import com.guardian.core.dagger.xml.XmlDataObjectFactory

data class FeedXmlDataObject(
    val title: String = "",
    val link: String = "",
    val description: String = "",
    val image: FeedImageXmlDataObject = FeedImageXmlDataObject(),
    val itunesImage: FeedItunesImageXmlDataObject = FeedItunesImageXmlDataObject(),
    val feedItems: List<FeedItemXmlDataObject> = listOf(FeedItemXmlDataObject())
) : XmlDataObject {
    override fun isEmpty(): Boolean = title.isEmpty() &&
            link.isEmpty() &&
            description.isEmpty() &&
            image.isEmpty() &&
            itunesImage.isEmpty() &&
            feedItems.isEmpty() || feedItems.map { it.isEmpty() }.reduce { acc, b -> acc && b }

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
                "item" to ValueContainer(listOf(FeedItemXmlDataObject()))
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
                xmlParserElementMap["item"]?.value as List<FeedItemXmlDataObject>
            )
        }
    }
}
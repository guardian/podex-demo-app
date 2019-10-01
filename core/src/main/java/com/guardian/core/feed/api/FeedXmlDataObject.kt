package com.guardian.core.feed.api

import com.guardian.core.lib.XmlDataObject
import com.guardian.core.lib.ValueContainer
import com.guardian.core.lib.XmlDataObjectFactory

data class FeedXmlDataObject (
    val title: String = "",
    val link: String = "",
    val description: String = "",
    val image: FeedImageXmlDataObject = FeedImageXmlDataObject(),
    val itunesImage: String = "",
    val feedItems: List<FeedItemXmlDataObject> = listOf(FeedItemXmlDataObject())
) : XmlDataObject {
    override val attributes: Map<String, ValueContainer<String>> = mapOf()

    companion object: XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mutableMapOf(
                "title" to ValueContainer(""),
                "link" to ValueContainer(""),
                "description" to ValueContainer(""),
                "image" to ValueContainer(FeedImageXmlDataObject()),
                "itunes:image" to ValueContainer(""),
                "item" to ValueContainer(listOf(FeedItemXmlDataObject()))
            )
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return FeedXmlDataObject(
                xmlParserElementMap["title"]?.value as String,
                xmlParserElementMap["link"]?.value as String,
                xmlParserElementMap["description"]?.value as String,
                xmlParserElementMap["image"]?.value as FeedImageXmlDataObject,
                xmlParserElementMap["itunes:image"]?.value as String,
                xmlParserElementMap["item"]?.value as List<FeedItemXmlDataObject>
            )
        }
    }
}
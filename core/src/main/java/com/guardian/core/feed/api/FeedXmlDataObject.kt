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
    companion object: XmlDataObjectFactory {
        override fun getXmlParserAttributeMap(): Map<String, ValueContainer<*>> {
            return mutableMapOf(
                "title" to ValueContainer(""),
                "link" to ValueContainer(""),
                "description" to ValueContainer(""),
                "image" to ValueContainer(FeedImageXmlDataObject()),
                "itunes:image" to ValueContainer(""),
                "item" to ValueContainer(listOf(FeedItemXmlDataObject()))
            )
        }

        override fun instantiateFromXmlParserAttributeMap(xmlParserAttributeMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return FeedXmlDataObject(
                xmlParserAttributeMap["title"]?.value as String,
                xmlParserAttributeMap["link"]?.value as String,
                xmlParserAttributeMap["description"]?.value as String,
                xmlParserAttributeMap["image"]?.value as FeedImageXmlDataObject,
                xmlParserAttributeMap["itunes:image"]?.value as String,
                xmlParserAttributeMap["item"]?.value as List<FeedItemXmlDataObject>
            )
        }
    }
}
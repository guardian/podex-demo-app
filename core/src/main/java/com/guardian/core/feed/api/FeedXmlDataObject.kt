package com.guardian.core.feed.api

import com.guardian.core.lib.XmlDataObject
import com.guardian.core.lib.ValueContainer

data class FeedXmlDataObject (
    val title: String = "",
    val link: String = "",
    val description: String = "",
    val image: FeedImageXmlDataObject = FeedImageXmlDataObject(),
    val itunesImage: String = ""
) : XmlDataObject {
    override fun getXmlParserAttributeMap(): MutableMap<String, ValueContainer<*>> {
        return mutableMapOf(
            "title" to ValueContainer(""),
            "link" to ValueContainer(""),
            "description" to ValueContainer(""),
            "image" to ValueContainer(FeedImageXmlDataObject()),
            "itunes:image" to ValueContainer("")
        )


    }

    override fun instantiateFromXmlParserAttributeMap(xmlParserAttibuteMap : MutableMap<String, ValueContainer<*>>): XmlDataObject {
        return FeedXmlDataObject(
            xmlParserAttibuteMap["title"]?.value as String,
            xmlParserAttibuteMap["link"]?.value as String,
            xmlParserAttibuteMap["description"]?.value as String,
            xmlParserAttibuteMap["image"]?.value as FeedImageXmlDataObject,
            xmlParserAttibuteMap["itunes:image"]?.value as String
        )
    }
}
package com.guardian.core.feed.api

import com.guardian.core.lib.ValueContainer
import com.guardian.core.lib.XmlDataObject

data class FeedImageXmlDataObject (
    val title: String = "",
    val url: String = "",
    val link: String = ""
) : XmlDataObject {
    override fun getXmlParserAttributeMap(): MutableMap<String, ValueContainer<*>> {
        return mutableMapOf(
            "title" to ValueContainer(""),
            "url" to ValueContainer(""),
            "link" to ValueContainer("")
        )
    }

    override fun instantiateFromXmlParserAttributeMap(xmlParserAttributeMap: MutableMap<String, ValueContainer<*>>): XmlDataObject {
        return FeedImageXmlDataObject(
            xmlParserAttributeMap["title"]?.value as String,
            xmlParserAttributeMap["url"]?.value as String,
            xmlParserAttributeMap["link"]?.value as String
            )
    }
}

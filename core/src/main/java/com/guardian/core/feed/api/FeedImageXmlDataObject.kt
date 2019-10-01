package com.guardian.core.feed.api

import com.guardian.core.lib.ValueContainer
import com.guardian.core.lib.XmlDataObject
import com.guardian.core.lib.XmlDataObjectFactory

data class FeedImageXmlDataObject (
    val title: String = "",
    val url: String = "",
    val link: String = ""
): XmlDataObject {
    companion object: XmlDataObjectFactory
    {
        override fun getXmlParserAttributeMap(): Map<String, ValueContainer<*>> {
            return mutableMapOf(
                "title" to ValueContainer(""),
                "url" to ValueContainer(""),
                "link" to ValueContainer("")
            )
        }

        override fun instantiateFromXmlParserAttributeMap(
            xmlParserAttributeMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return FeedImageXmlDataObject(
                xmlParserAttributeMap["title"]?.value as String,
                xmlParserAttributeMap["url"]?.value as String,
                xmlParserAttributeMap["link"]?.value as String
            )
        }
    }
}
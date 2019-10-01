package com.guardian.core.feed.api

import com.guardian.core.lib.ValueContainer
import com.guardian.core.lib.XmlDataObject
import com.guardian.core.lib.XmlDataObjectFactory

data class FeedImageXmlDataObject (
    val title: String = "",
    val url: String = "",
    val link: String = ""
): XmlDataObject {
    override val attributes: Map<String, ValueContainer<String>> = mapOf()

    companion object: XmlDataObjectFactory
    {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mapOf(
                "title" to ValueContainer(""),
                "url" to ValueContainer(""),
                "link" to ValueContainer("")
            )
        }

        override fun instantiateFromXmlParserElementMap(
            xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return FeedImageXmlDataObject(
                xmlParserElementMap["title"]?.value as String,
                xmlParserElementMap["url"]?.value as String,
                xmlParserElementMap["link"]?.value as String
            )
        }
    }
}
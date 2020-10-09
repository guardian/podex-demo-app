package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

data class FeedImageXmlDataObject(
    val title: String = "",
    val url: String = "",
    val link: String = ""
) : XmlDataObject {
    override fun isEmpty(): Boolean =
        title.isEmpty() &&
            url.isEmpty() &&
            link.isEmpty()

    override val attributes: Map<String, ValueContainer<String>> = mapOf()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mapOf(
                "title" to ValueContainer(""),
                "url" to ValueContainer(""),
                "link" to ValueContainer("")
            )
        }

        override fun instantiateFromXmlParserElementMap(
            xmlParserElementMap: Map<String, ValueContainer<*>>
        ): XmlDataObject {
            return FeedImageXmlDataObject(
                xmlParserElementMap["title"]?.value as String,
                xmlParserElementMap["url"]?.value as String,
                xmlParserElementMap["link"]?.value as String
            )
        }
    }
}

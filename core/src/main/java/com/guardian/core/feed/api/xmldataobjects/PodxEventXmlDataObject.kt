package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

data class PodxEventXmlDataObject(
    val start: String = "",
    val end: String = "",
    val caption: String = "",
    val notification: String = ""
) : XmlDataObject {
    override fun isEmpty(): Boolean = true

    override val attributes: Map<String, ValueContainer<String>> = mapOf(
        "href" to ValueContainer("")
    )

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mutableMapOf(
                "start" to ValueContainer(""),
                "end" to ValueContainer(""),
                "caption" to ValueContainer(""),
                "notification" to ValueContainer("")
            )
        }

        @Suppress("UNCHECKED_CAST")
        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return PodxEventXmlDataObject(
                xmlParserElementMap["start"]?.value as String,
                xmlParserElementMap["end"]?.value as String,
                xmlParserElementMap["caption"]?.value as String,
                xmlParserElementMap["notification"]?.value as String
            )
        }
    }
}
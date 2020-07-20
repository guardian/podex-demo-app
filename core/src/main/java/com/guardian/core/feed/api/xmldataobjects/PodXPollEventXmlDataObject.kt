package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

data class PodXPollEventXmlDataObject(
    val start: String = "",
    val end: String = "",
    val caption: String = "",
    val notification: String = ""
) : XmlDataObject {
    override fun isEmpty(): Boolean = start.isEmpty() &&
        attributes.values
            .map { it.value.isEmpty() }
            .reduce { acc, b -> acc && b }

    override val attributes: Map<String, ValueContainer<String>> = mapOf(
        "url" to ValueContainer("")
    )

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mutableMapOf(
                "podx:start" to ValueContainer(""),
                "podx:end" to ValueContainer(""),
                "podx:caption" to ValueContainer(""),
                "podx:notification" to ValueContainer("")
            )
        }

        @Suppress("UNCHECKED_CAST")
        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return PodXPollEventXmlDataObject(
                xmlParserElementMap["podx:start"]?.value as String,
                xmlParserElementMap["podx:end"]?.value as String,
                xmlParserElementMap["podx:caption"]?.value as String,
                xmlParserElementMap["podx:notification"]?.value as String
            )
        }
    }
}

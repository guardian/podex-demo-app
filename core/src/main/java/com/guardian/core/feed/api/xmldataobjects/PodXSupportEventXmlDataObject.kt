package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

data class PodXSupportEventXmlDataObject(
    val start: String = "",
    val end: String = "",
    val caption: String = "",
    val notification: String = ""
) : XmlDataObject {
    override val attributes: Map<String, ValueContainer<String>> =
        mapOf(
            "url" to ValueContainer("")
        )

    override fun isEmpty(): Boolean =
        (attributes["url"]?.value).isNullOrBlank()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mapOf(
                "podx:start" to ValueContainer(""),
                "podx:end" to ValueContainer(""),
                "podx:caption" to ValueContainer(""),
                "podx:notification" to ValueContainer("")
            )
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return PodXSupportEventXmlDataObject(
                xmlParserElementMap["podx:start"]?.value as String,
                xmlParserElementMap["podx:end"]?.value as String,
                xmlParserElementMap["podx:caption"]?.value as String,
                xmlParserElementMap["podx:notification"]?.value as String
            )
        }
    }
}
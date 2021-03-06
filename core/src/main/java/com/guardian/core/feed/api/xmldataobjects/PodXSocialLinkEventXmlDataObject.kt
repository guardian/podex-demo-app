package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

@Suppress("UNCHECKED_CAST")
data class PodXSocialLinkEventXmlDataObject(
    val start: String = "",
    val end: String = "",
    val caption: String = "",
    val notification: String = "",
    val socialLink: List<PodXSocialLinkXmlDataObject> = listOf(PodXSocialLinkXmlDataObject())
) : XmlDataObject {
    override val attributes: Map<String, ValueContainer<String>> =
        mapOf()

    override fun isEmpty(): Boolean =
        caption.isBlank()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mapOf<String, ValueContainer<*>>(
                "podx:start" to ValueContainer(""),
                "podx:end" to ValueContainer(""),
                "podx:caption" to ValueContainer(""),
                "podx:notification" to ValueContainer(""),
                "podx:socialType" to ValueContainer(listOf(PodXSocialLinkXmlDataObject()))
            )
        }

        override fun instantiateFromXmlParserElementMap
        (xmlParserElementMap: Map<String, ValueContainer<*>>):
            XmlDataObject {

                return PodXSocialLinkEventXmlDataObject(
                    xmlParserElementMap["podx:start"]?.value as String,
                    xmlParserElementMap["podx:end"]?.value as String,
                    xmlParserElementMap["podx:caption"]?.value as String,
                    xmlParserElementMap["podx:notification"]?.value as String,
                    xmlParserElementMap["podx:socialType"]?.value as List<PodXSocialLinkXmlDataObject>
                )
            }
    }
}

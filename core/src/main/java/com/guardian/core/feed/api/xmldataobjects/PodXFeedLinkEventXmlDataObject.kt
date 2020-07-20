package com.guardian.core.feed.api.xmldataobjects
import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

data class PodXFeedLinkEventXmlDataObject(
    val start: String = "",
    val end: String = "",
    val caption: String = "",
    val notification: String = "",
    val feedUrl: String = "",
    val feedItemTitle: String = "",
    val feedItemPubDate: String = "",
    val feedItemGuid: String = "",
    val feedItemEnclosureUrl: String = "",
    val feedItemAudioTimestamp: String = ""
) : XmlDataObject {
    override val attributes: Map<String, ValueContainer<String>> =
        mapOf()

    override fun isEmpty(): Boolean =
        start.isBlank() ||
            feedItemEnclosureUrl.isBlank()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mapOf<String, ValueContainer<*>>(
                "podx:start" to ValueContainer(""),
                "podx:end" to ValueContainer(""),
                "podx:caption" to ValueContainer(""),
                "podx:notification" to ValueContainer(""),
                "podx:feedUrl" to ValueContainer(""),
                "podx:feedItemTitle" to ValueContainer(""),
                "podx:feedItemPubDate" to ValueContainer(""),
                "podx:feedItemGuid" to ValueContainer(""),
                "podx:feedItemEnclosureUrl" to ValueContainer(""),
                "podx:feedItemAudioTimestamp" to ValueContainer("")
            )
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return PodXFeedLinkEventXmlDataObject(
                xmlParserElementMap["podx:start"]?.value as String,
                xmlParserElementMap["podx:end"]?.value as String,
                xmlParserElementMap["podx:caption"]?.value as String,
                xmlParserElementMap["podx:notification"]?.value as String,
                xmlParserElementMap["podx:feedUrl"]?.value as String,
                xmlParserElementMap["podx:feedItemTitle"]?.value as String,
                xmlParserElementMap["podx:feedItemPubDate"]?.value as String,
                xmlParserElementMap["podx:feedItemGuid"]?.value as String,
                xmlParserElementMap["podx:feedItemEnclosureUrl"]?.value as String,
                xmlParserElementMap["podx:feedItemAudioTimestamp"]?.value as String
            )
        }
    }
}

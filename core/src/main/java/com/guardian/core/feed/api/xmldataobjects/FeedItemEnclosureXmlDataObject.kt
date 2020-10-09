package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

class FeedItemEnclosureXmlDataObject : XmlDataObject {
    override fun isEmpty(): Boolean = attributes.values
        .map { it.value.isEmpty() }
        .reduce { acc, b -> acc || b }

    override val attributes: Map<String, ValueContainer<String>> = mapOf(
        "url" to ValueContainer(""),
        "length" to ValueContainer(""),
        "type" to ValueContainer("")
    )

    override fun toString(): String {
        return attributes.toString()
    }

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> = mapOf()

        override fun instantiateFromXmlParserElementMap
        (xmlParserElementMap: Map<String, ValueContainer<*>>):
            XmlDataObject = FeedItemEnclosureXmlDataObject()
    }
}

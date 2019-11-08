package com.guardian.core.feed.api

import com.guardian.core.dagger.xml.ValueContainer
import com.guardian.core.dagger.xml.XmlDataObject
import com.guardian.core.dagger.xml.XmlDataObjectFactory

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
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mapOf()
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return FeedItemEnclosureXmlDataObject()
        }
    }
}

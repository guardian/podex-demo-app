package com.guardian.core.feed.api

import com.guardian.core.lib.ValueContainer
import com.guardian.core.lib.XmlDataObject
import com.guardian.core.lib.XmlDataObjectFactory

/**
 * empty data object used to get attributes
 */
class FeedItunesImageXmlDataObject : XmlDataObject {
    override fun isEmpty(): Boolean = attributes.values
        .map { it.value.isEmpty()}
        .reduce { acc, b -> acc && b }

    override val attributes: Map<String, ValueContainer<String>> = mapOf(
        "href" to ValueContainer("")
    )

    companion object: XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mapOf()
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return FeedItunesImageXmlDataObject()
        }

    }
}
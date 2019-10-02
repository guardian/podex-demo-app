package com.guardian.core.feed.api

import com.guardian.core.lib.ValueContainer
import com.guardian.core.lib.XmlDataObject
import com.guardian.core.lib.XmlDataObjectFactory

data class RootXmlDataObject (
    val rssRoot: RssRootXmlDataObject = RssRootXmlDataObject()
): XmlDataObject {
    override fun isEmpty(): Boolean = rssRoot.isEmpty()

    override val attributes: Map<String, ValueContainer<String>>
        get() = mapOf()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mapOf(
                "rss" to ValueContainer(RssRootXmlDataObject())
            )
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return RootXmlDataObject(
                xmlParserElementMap["rss"]?.value as RssRootXmlDataObject
            )
        }
    }
}
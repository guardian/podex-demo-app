package com.guardian.core.feed.api

import com.guardian.core.dagger.xml.ValueContainer
import com.guardian.core.dagger.xml.XmlDataObject
import com.guardian.core.dagger.xml.XmlDataObjectFactory

data class RssRootXmlDataObject(
    val feeds: List<FeedXmlDataObject> = listOf(FeedXmlDataObject())
) : XmlDataObject {
    override fun isEmpty(): Boolean = feeds.isEmpty()

    override val attributes: Map<String, ValueContainer<String>> = mapOf()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mapOf(
                "channel" to ValueContainer(listOf(FeedXmlDataObject()))
            )
        }

        @Suppress("UNCHECKED_CAST")
        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return RssRootXmlDataObject(
                xmlParserElementMap["channel"]?.value as List<FeedXmlDataObject>
            )
        }
    }
}
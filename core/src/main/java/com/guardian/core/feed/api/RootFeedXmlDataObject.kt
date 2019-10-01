package com.guardian.core.feed.api

import com.guardian.core.lib.ValueContainer
import com.guardian.core.lib.XmlDataObject
import com.guardian.core.lib.XmlDataObjectFactory

data class RootFeedXmlDataObject (
    val feeds: List<FeedXmlDataObject> = listOf(FeedXmlDataObject())
) : XmlDataObject {
    override val attributes: Map<String, ValueContainer<String>> = mapOf()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return  mapOf(
                "channel" to ValueContainer(listOf(FeedXmlDataObject()))
            )
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return RootFeedXmlDataObject(
                xmlParserElementMap["channel"]?.value as List<FeedXmlDataObject>
            )
        }

    }
}
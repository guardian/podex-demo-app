package com.guardian.core.feed.api

import com.guardian.core.lib.ValueContainer
import com.guardian.core.lib.XmlDataObject
import com.guardian.core.lib.XmlDataObjectFactory

data class FeedItemXmlDataObject (
    val title: String = ""
) : XmlDataObject {
    override fun isEmpty(): Boolean = title.isEmpty()

    override val attributes: Map<String, ValueContainer<String>> = mapOf()

    companion object: XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mutableMapOf(
                "title" to ValueContainer("")
            )
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return FeedItemXmlDataObject(
                xmlParserElementMap["title"]?.value as String
            )
        }
    }

}
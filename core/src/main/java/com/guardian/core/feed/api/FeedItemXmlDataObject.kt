package com.guardian.core.feed.api

import com.guardian.core.lib.ValueContainer
import com.guardian.core.lib.XmlDataObject
import com.guardian.core.lib.XmlDataObjectFactory

data class FeedItemXmlDataObject (
    val title: String = ""
) : XmlDataObject {
    companion object: XmlDataObjectFactory {
        override fun getXmlParserAttributeMap(): Map<String, ValueContainer<*>> {
            return mutableMapOf(
                "title" to ValueContainer("")
            )
        }

        override fun instantiateFromXmlParserAttributeMap(xmlParserAttributeMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return FeedItemXmlDataObject(
                xmlParserAttributeMap["title"]?.value as String
            )
        }
    }

}
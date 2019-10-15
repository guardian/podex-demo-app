package com.guardian.core.feed.api

import com.guardian.core.dagger.xml.ValueContainer
import com.guardian.core.dagger.xml.XmlDataObject
import com.guardian.core.dagger.xml.XmlDataObjectFactory

data class FeedItunesOwnerXmlDataObject(
    val name: String = "",
    val email: String = ""
) : XmlDataObject {
    override val attributes: Map<String, ValueContainer<String>> = mapOf()

    override fun isEmpty(): Boolean = name.isEmpty()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mapOf(
                "itunes:name" to ValueContainer(""),
                "itunes:email" to ValueContainer("")
            )
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): FeedItunesOwnerXmlDataObject {
            return FeedItunesOwnerXmlDataObject(
                xmlParserElementMap["itunes:name"]?.value as String,
                xmlParserElementMap["itunes:email"]?.value as String
            )
        }
    }
}

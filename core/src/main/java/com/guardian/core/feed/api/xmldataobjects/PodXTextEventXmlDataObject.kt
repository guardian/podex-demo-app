package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

data class PodXTextEvent (
    val start: String,
    val end: String,
    val caption: String,
    val notification: String
) : XmlDataObject {
    override val attributes: Map<String, ValueContainer<String>> =
        mapOf()

    override fun isEmpty(): Boolean {

    }

    companion object: XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}
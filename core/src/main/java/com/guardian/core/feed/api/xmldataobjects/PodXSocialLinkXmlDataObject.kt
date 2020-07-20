package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

class PodXSocialLinkXmlDataObject : XmlDataObject {
    override val attributes: Map<String, ValueContainer<String>> =
        mapOf(
            "url" to ValueContainer("")
        )

    override fun isEmpty(): Boolean =
        (attributes["url"]?.value).isNullOrBlank()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> = mapOf()

        override fun instantiateFromXmlParserElementMap
        (xmlParserElementMap: Map<String, ValueContainer<*>>):
            XmlDataObject = PodXSocialLinkXmlDataObject()
    }
}

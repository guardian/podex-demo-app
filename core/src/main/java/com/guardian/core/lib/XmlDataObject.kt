package com.guardian.core.lib

interface XmlDataObject {
    fun getXmlParserAttributeMap(): MutableMap<String, ValueContainer<*>>
    fun instantiateFromXmlParserAttributeMap(xmlParserAttributeMap: MutableMap<String, ValueContainer<*>>): XmlDataObject
}
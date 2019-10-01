package com.guardian.core.lib

import java.io.InvalidClassException
import kotlin.reflect.full.companionObjectInstance

/**
 * The general form of out Xml Data Objects set up for easy de-serialization
 */
interface XmlDataObject

/**
 * Get factory and check by reflection if the data class if constructed correctly, if not then we'll
 * throw a runtime exception.
 */
val XmlDataObject.factory: XmlDataObjectFactory
    get() = if (this::class.companionObjectInstance is XmlDataObjectFactory) {
            this::class.companionObjectInstance as XmlDataObjectFactory
        } else {
            throw InvalidClassException(
                "Xml Data objects need a XmlDataObjectFactory companion object ")
        }

/**
 * Interface for the companion object of a [XmlDataObject]
 */
interface XmlDataObjectFactory {
    /**
     * Returns a map of strings representing xml tag names and a mutable container with the
     * corresponding value
     */
    fun getXmlParserAttributeMap(): Map<String, ValueContainer<*>>

    /**
     * Instantiates a new [XmlDataObject]
     *
     * @param xmlParserAttributeMap should come from an instance of the same type otherwise it will
     */
    fun instantiateFromXmlParserAttributeMap(
        xmlParserAttributeMap: Map<String, ValueContainer<*>>): XmlDataObject
}
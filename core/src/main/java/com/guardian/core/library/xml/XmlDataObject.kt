package com.guardian.core.library.xml

import java.io.InvalidClassException
import kotlin.reflect.full.companionObjectInstance

/**
 * The general form of out Xml Data Objects set up for easy de-serialization.
 */
interface XmlDataObject {
    val attributes: Map<String, ValueContainer<String>>
    fun isEmpty(): Boolean
}

/**
 * Get factory and check by reflection if the data class if constructed correctly, if not then we'll
 * throw a runtime exception.
 */
val XmlDataObject.factory: XmlDataObjectFactory
    get() = if (this::class.companionObjectInstance is XmlDataObjectFactory) {
            this::class.companionObjectInstance as XmlDataObjectFactory
        } else {
            throw InvalidClassException(
                "Xml Data objects need a XmlDataObjectFactory companion object "
            )
        }

/**
 * Interface for the companion object of a [XmlDataObject]
 */
interface XmlDataObjectFactory {
    /**
     * The [getXmlParserElementMap] is hard coded to match the constructor of a given data object
     * and manually passed to the [instantiateFromXmlParserElementMap] method by the
     * [XmlPullParserAdapter]. If it is not correctly formed it will crash at runtime.
     *
     * @return A map of strings representing xml tag names and a mutable container with the
     * corresponding value
     */
    fun getXmlParserElementMap(): Map<String, ValueContainer<*>>

    /**
     * Instantiates a new [XmlDataObject]
     *
     * @param xmlParserElementMap should come from an instance of the same type otherwise it will
     */
    fun instantiateFromXmlParserElementMap(
        xmlParserElementMap: Map<String, ValueContainer<*>>
    ): XmlDataObject
}
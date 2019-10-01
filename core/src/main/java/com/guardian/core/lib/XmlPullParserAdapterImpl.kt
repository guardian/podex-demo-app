package com.guardian.core.lib

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.*
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.io.InvalidClassException
import java.nio.charset.StandardCharsets
import javax.inject.Inject


/**
 * A crude pull parser implementation for de-serialising XML documents. Currently only set up to
 * produce instances of [com.guardian.core.feed.api.RootFeedXmlDataObject] and it's children.
 *
 * Maps for the values stored in each object can be taken from the [XmlDataObject]'s
 * factory, which is the type of the companion object. it is assumed the objects are perfectly
 * formed.
 *
 * The only supported attribute types for [XmlDataObject] mappings are currently:
 * [String]
 * [XmlDataObject]
 * [List]<[XmlDataObject]>
 */

class XmlPullParserAdapterImpl
@Inject constructor(val xmlPullParserFactory: XmlPullParserFactory)
    : XmlPullParserAdapter {

    @Throws(XmlPullParserException::class, IOException::class, InvalidClassException::class)
    override suspend fun deSerialiseXml(xmlInput: InputStream, rootDataObjectInitializer: () -> XmlDataObject): XmlDataObject {
        val xmlPullParser = xmlPullParserFactory.newPullParser()

        xmlPullParser.setInput(xmlInput.reader(StandardCharsets.UTF_8))
        while (xmlPullParser.eventType != START_DOCUMENT) {
            print(xmlPullParser.text)
            xmlPullParser.next()
        }

        return deSerializeBody(xmlPullParser, rootDataObjectInitializer())
    }

    /**
     * Recursively fill the XmlDataObjects using the pull parser, using the arg to instantiate a
     * new data object.
     */
    @Throws(XmlPullParserException::class, IOException::class, InvalidClassException::class)
    private fun deSerializeBody(xmlPullParser: XmlPullParser, xmlDataObject: XmlDataObject): XmlDataObject {
        var eventType = xmlPullParser.getEventType()
        val currentDepth = xmlPullParser.depth
        val elementMap = xmlDataObject.factory.getXmlParserElementMap()
        var currentElement: ValueContainer<String>? = null

        val xmlAttributeValueMap = mutableMapOf<String, String>()
        for (attributeIndex in 0..xmlPullParser.attributeCount) {
            xmlAttributeValueMap[xmlPullParser.getAttributeName(attributeIndex)] =
                xmlPullParser.getAttributeValue(attributeIndex)
        }

        //handle self closing tags
        if (!xmlPullParser.isEmptyElementTag) {
            xmlPullParser.next()

            while (eventType != END_DOCUMENT && xmlPullParser.depth >= currentDepth) {
                if (eventType == START_TAG) {
                    val currentName = xmlPullParser.getName()
                    val attributeCheck = elementMap[currentName]

                    if (attributeCheck == null) {
                        skip(xmlPullParser)
                    } else if (attributeCheck.value is String && !xmlPullParser.isEmptyElementTag) {
                        @Suppress("UNCHECKED_CAST")
                        currentElement = attributeCheck as ValueContainer<String>
                    } else {
                        // recurse here to instantiate a new data object as a child
                        val checkElementValue = attributeCheck.value
                        if (checkElementValue is List<*>) {
                            val mutableNewList = mutableListOf<XmlDataObject>()
                            checkElementValue.forEach {
                                if (it is XmlDataObject) {
                                    mutableNewList.add(it)
                                }
                            }

                            mutableNewList.add(
                                deSerializeBody(
                                    xmlPullParser,
                                    mutableNewList.first()
                                )
                            )

                            (elementMap[currentName] as ValueContainer<List<XmlDataObject>>)
                                .value = mutableNewList.toList()
                        } else if (checkElementValue is XmlDataObject) {
                            (elementMap[currentName] as ValueContainer<XmlDataObject>)
                                .value = deSerializeBody(xmlPullParser, checkElementValue)
                        }
                    }
                } else if (eventType == END_TAG) {
                    currentElement = null
                } else if (eventType == TEXT) {
                    currentElement?.value = xmlPullParser.text
                }
                eventType = xmlPullParser.next()
            }
        } else {
            xmlPullParser.next()
        }

        return xmlDataObject.factory.instantiateFromXmlParserElementMap(elementMap).apply {
            this.attributes.keys.forEach{
                val xmlAttributeValue = xmlAttributeValueMap[it]
                if (xmlAttributeValue != null) {
                    this.attributes[it]?.value = xmlAttributeValue
                }
            }
        }
    }

    /**
     * handy function from the android docs
     */
    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                END_TAG -> depth--
                START_TAG -> depth++
            }
        }
    }
}


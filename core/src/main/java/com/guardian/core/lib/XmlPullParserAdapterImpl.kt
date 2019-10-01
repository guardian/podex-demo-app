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
 * produce instances of [com.guardian.core.feed.Feed]
 *
 * Maps for the values stored in each object can be taken from the [XmlDataObject]'s
 * factory, which is the type of the companion object.
 *
 * The only supported attribute types for [XmlDataObject] mappings are currently:
 *      [String]
 *      [XmlDataObject]
 *      [List<XmlDataObject>]
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
     * recursively fill the XmlDataObjects using the pull parser, using the arg to instantiate a
     * new data object.
     */
    @Throws(XmlPullParserException::class, IOException::class, InvalidClassException::class)
    private fun deSerializeBody(xmlPullParser: XmlPullParser, xmlDataObject: XmlDataObject): XmlDataObject {
        var eventType = xmlPullParser.getEventType()
        val currentDepth = xmlPullParser.depth
        val attributeMap = xmlDataObject.factory.getXmlParserAttributeMap()
        var currentAttribute: ValueContainer<String>? = null

        while (eventType != END_DOCUMENT && xmlPullParser.depth >= currentDepth) {
            if (eventType == START_TAG) {
                val attributeCheck = attributeMap[xmlPullParser.getName()]

                if (attributeCheck == null) {
                    skip(xmlPullParser)
                } else if (attributeCheck.value is String) {
                    @Suppress("UNCHECKED_CAST")
                    currentAttribute = attributeCheck as ValueContainer<String>
                } else {
                    // recurse here to instantiate a new data object as a child
//                    if (attributeCheck.value is List<*>) {
//                        xmlPullParser.next()
//                        attributeCheck.value = attributeCheck.value + listOf(deSerializeBody(xmlPullParser, attributeCheck.value.first() as XmlDataObject))
//                    } else if (attributeCheck.value is XmlDataObject) {
//                        val recursableAttribute
//
//                        xmlPullParser.next()
//                        attributeCheck.value = deSerializeBody(xmlPullParser, attributeCheck.value)
//                    }
                }
            } else if (eventType == END_TAG) {
                currentAttribute = null
            } else if (eventType == TEXT) {
                currentAttribute?.value = xmlPullParser.text
            }
            eventType = xmlPullParser.next()
        }

        return xmlDataObject.factory.instantiateFromXmlParserAttributeMap(attributeMap)
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


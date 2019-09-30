package com.guardian.core.lib

import com.guardian.core.feed.api.FeedXmlDataObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.*
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.io.InvalidClassException
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import javax.inject.Inject


/**
 * A simple pull parser adapter for de-serialising XML documents to [Feed] data objects. The [Feed]
 * and it's children, [FeedItem] and [PodexEvent]. Mutable maps for the values stored in each object
 * from the
 */

class XmlPullParserAdapterImpl
@Inject constructor(val xmlPullParserFactory: XmlPullParserFactory)
    : XmlPullParserAdapter {

    override suspend fun deSerialiseXml(xmlInput: InputStream, dataObject: XmlDataObject): XmlDataObject {
        val xmlPullParser = xmlPullParserFactory.newPullParser()

        //todo get charset from xml, assume utf8
        xmlPullParser.setInput(xmlInput.reader(StandardCharsets.UTF_8))
        while (xmlPullParser.eventType != START_DOCUMENT) {
            print(xmlPullParser.text)
            xmlPullParser.next()
        }

        return deSerializeBody(xmlPullParser, dataObject)
    }

    // current element = blank
    // map of elements to values
    //while not end of document
        //if start tag
            //if tag matches list data param
                // map listdataparam element to deserialisebody()
            //if tag matches string param
                //current param = that string param
        //if text
            //if current element is set
                //map current element to text
        //if end tag
            // current element = blank
            // if tag name = this root type
                // return new instance with value of map

    // return new instance with value of map

    private fun deSerializeBody(xmlPullParser: XmlPullParser, xmlDataObject: XmlDataObject): XmlDataObject {

        var eventType = xmlPullParser.getEventType()
        val attributeMap: MutableMap<String, ValueContainer<*>> = xmlDataObject.getXmlParserAttributeMap()

        var currentAttribute: ValueContainer<String>? = null

        while (eventType != END_DOCUMENT) {
            if (eventType == START_TAG) {
                //System.out.println("Start tag " + xmlPullParser.getName())
            } else if (eventType == END_TAG) {
                //System.out.println("End tag " + xmlPullParser.getName())
            } else if (eventType == TEXT) {
                //System.out.println("Text " + xmlPullParser.getText())
            }
            eventType = xmlPullParser.next()
        }

        return xmlDataObject.instantiateFromXmlParserAttributeMap(attributeMap)
    }

//    /**
//     * Prefill map with default constructor values for collections and strings
//     */
//    private fun constructAttributeMap(type: Type): MutableMap<Type, ValueContainer<*>> {
//        val keys = getParamTypes(type)
//        val map = mutableMapOf<Type, ValueContainer<*>>()
//
//
//        Class.forName(type.typeName)
//            .declaredFields
//            .forEach {
//                if (it.type == String::class.java) {
//                    map[it.type] = ValueContainer("")
//                } else if (it.type == List::class.java) {
//                    //extract the generic type
//                    val genericName = it.genericType
//                        .typeName
//                        .dropWhile { c -> c != '<' }
//                        .removeSuffix(">")
//                        .removePrefix("<")
//
//                    val genericType = Class.forName(genericName)
//
//                    map[genericType] = ValueContainer(mutableListOf<ValueContainer<*>>())
//                }
//            }
//        for (key in keys) {
//            if (key?.typeName == String::class.java.typeName) {
//                map[key] = ValueContainer("")
//                System.out.println(key.typeName)
//            } else {
//                if (key != null) {
//                    System.out.println(Class.forName(key.typeName).canonicalName)
//                    System.out.println(Class.forName(key.typeName).typeName)
//                    System.out.println(key.typeName)
//                }
//            }
//        }
//
//        return map
//    }

//
//    private fun getParamTypes(baseType: Type): List<Type?> {
//        val baseClass = Class.forName(baseType.typeName)
//
//        if (baseClass.declaredFields.isEmpty()) {
//            throw InvalidClassException("Class ${baseClass.name} needs a constructor")
//        }
//
//        return baseClass.declaredFields.map {
//            it.type.componentType ?: it.type
//        }
//    }
}


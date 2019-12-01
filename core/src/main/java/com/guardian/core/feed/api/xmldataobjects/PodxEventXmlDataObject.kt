package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

data class PodxEventXmlDataObject(
    val start: String = "",
    val end: String = "",
    val caption: String = "",
    val notification: String = ""
) : XmlDataObject {
    override fun isEmpty(): Boolean = start.isEmpty() &&
        attributes.values
            .map { it.value.isEmpty() }
            .reduce { acc, b -> acc && b }

    override val attributes: Map<String, ValueContainer<String>> = mapOf(
        "href" to ValueContainer("")
    )

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mutableMapOf(
                "podx:start" to ValueContainer(""),
                "podx:end" to ValueContainer(""),
                "podx:caption" to ValueContainer(""),
                "podx:notification" to ValueContainer("")
            )
        }

        @Suppress("UNCHECKED_CAST")
        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return PodxEventXmlDataObject(
                xmlParserElementMap["podx:start"]?.value as String,
                xmlParserElementMap["podx:end"]?.value as String,
                xmlParserElementMap["podx:caption"]?.value as String,
                xmlParserElementMap["podx:notification"]?.value as String
            )
        }
    }
}

/**
 *
 */
fun String.parseNormalPlayTimeToMillis(): Long {
    trim().split(Regex(":"), 3)
        .apply {
            return when (size) {
                1 -> {
                    // expect the format is SS.mmm
                    (this[0].toDouble() * 1000).toLong()
                }
                2 -> {
                    // expect the format MM.SS.mmm
                    (this[0].toLong() * 60000 + (this[1].toDouble() * 1000)).toLong()
                }

                3 -> {
                    // expect the format HH:MM:SS.mmm
                    (this[0].toLong() * 3600000 + this[1].toLong() * 60000 +
                    (this[2].toDouble() * 1000)).toLong()
                }

                else -> {
                    throw IllegalArgumentException("Invalid NPT time stamp")
                }
            }
        }
}

fun String.parseNormalPlayTimeToMillisOrNull(): Long? {
    return try {
        parseNormalPlayTimeToMillis()
    } catch (e: Exception) {
        null
    }
}
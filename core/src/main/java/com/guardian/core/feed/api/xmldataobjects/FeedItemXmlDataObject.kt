package com.guardian.core.feed.api.xmldataobjects

import com.guardian.core.library.xml.ValueContainer
import com.guardian.core.library.xml.XmlDataObject
import com.guardian.core.library.xml.XmlDataObjectFactory

@Suppress("UNCHECKED_CAST")
data class FeedItemXmlDataObject(
    val title: String = "",
    val description: String = "",
    val itunesImage: FeedItunesImageXmlDataObject = FeedItunesImageXmlDataObject(),
    val image: FeedImageXmlDataObject = FeedImageXmlDataObject(),
    val pubDate: String = "",
    val enclosureXmlDataObject: FeedItemEnclosureXmlDataObject = FeedItemEnclosureXmlDataObject(),
    val duration: String = "",
    val author: String = "",
    val keywords: String = "",
    val podxImages: List<PodXImageEventXmlDataObject> = listOf(PodXImageEventXmlDataObject()),
    val podxWeb: List<PodXWebEventXmlDataObject> = listOf(PodXWebEventXmlDataObject()),
    val podxSupport: List<PodXSupportEventXmlDataObject> = listOf(PodXSupportEventXmlDataObject()),
    val podxSocialLinkEventXmlDataObject: List<PodXSocialLinkEventXmlDataObject> = listOf(PodXSocialLinkEventXmlDataObject()),
    val podXTextEventXmlDataObject: List<PodXTextEventXmlDataObject> = listOf(PodXTextEventXmlDataObject()),
    val podXFeedLinkEventXmlDataObject: List<PodXFeedLinkEventXmlDataObject> = listOf(PodXFeedLinkEventXmlDataObject()),
    val podXCallPromptEventXmlDataObject: List<PodXCallPromptEventXmlDataObject> = listOf(PodXCallPromptEventXmlDataObject())
) : XmlDataObject {
    override fun isEmpty(): Boolean = enclosureXmlDataObject.isEmpty()

    override val attributes: Map<String, ValueContainer<String>> = mapOf()

    companion object : XmlDataObjectFactory {
        override fun getXmlParserElementMap(): Map<String, ValueContainer<*>> {
            return mapOf(
                "title" to ValueContainer(""),
                "description" to ValueContainer(""),
                "itunes:image" to ValueContainer(FeedItunesImageXmlDataObject()),
                "image" to ValueContainer(FeedImageXmlDataObject()),
                "pubDate" to ValueContainer(""),
                "enclosure" to ValueContainer(FeedItemEnclosureXmlDataObject()),
                "itunes:duration" to ValueContainer(""),
                "itunes:author" to ValueContainer(""),
                "itunes:keywords" to ValueContainer(""),
                "podx:image" to ValueContainer(listOf(PodXImageEventXmlDataObject())),
                "podx:webLink" to ValueContainer(listOf(PodXWebEventXmlDataObject())),
                "podx:support" to ValueContainer(listOf(PodXSupportEventXmlDataObject())),
                "podx:socialPrompt" to ValueContainer(listOf(PodXSocialLinkEventXmlDataObject())),
                "podx:text" to ValueContainer(listOf(PodXTextEventXmlDataObject())),
                "podx:feedLink" to ValueContainer(listOf(PodXFeedLinkEventXmlDataObject())),
                "podx:callPrompt" to ValueContainer(listOf(PodXCallPromptEventXmlDataObject()))
            )
        }

        override fun instantiateFromXmlParserElementMap(xmlParserElementMap: Map<String, ValueContainer<*>>): XmlDataObject {
            return FeedItemXmlDataObject(
                title = xmlParserElementMap["title"]?.value as String,
                description = xmlParserElementMap["description"]?.value as String,
                itunesImage = xmlParserElementMap["itunes:image"]?.value
                    as FeedItunesImageXmlDataObject,
                image = xmlParserElementMap["image"]?.value as FeedImageXmlDataObject,
                pubDate = xmlParserElementMap["pubDate"]?.value as String,
                enclosureXmlDataObject = xmlParserElementMap["enclosure"]?.value
                    as FeedItemEnclosureXmlDataObject,
                duration = xmlParserElementMap["itunes:duration"]?.value as String,
                author = xmlParserElementMap["itunes:author"]?.value as String,
                keywords = xmlParserElementMap["itunes:keywords"]?.value as String,
                podxImages = xmlParserElementMap["podx:image"]?.value as List<PodXImageEventXmlDataObject>,
                podxWeb = xmlParserElementMap["podx:webLink"]?.value as List<PodXWebEventXmlDataObject>,
                podxSupport = xmlParserElementMap["podx:support"]?.value as List<PodXSupportEventXmlDataObject>,
                podxSocialLinkEventXmlDataObject = xmlParserElementMap["podx:socialPrompt"]?.value as List<PodXSocialLinkEventXmlDataObject>,
                podXTextEventXmlDataObject = xmlParserElementMap["podx:text"]?.value as List<PodXTextEventXmlDataObject>,
                podXFeedLinkEventXmlDataObject = xmlParserElementMap["podx:feedLink"]?.value as List<PodXFeedLinkEventXmlDataObject>,
                podXCallPromptEventXmlDataObject = xmlParserElementMap["podx:callPrompt"]?.value as List<PodXCallPromptEventXmlDataObject>
            )
        }
    }
}
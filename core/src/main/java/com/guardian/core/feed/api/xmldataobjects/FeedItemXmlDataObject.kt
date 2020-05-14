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
    val guid: String = "",
    val podxImages: List<PodXImageEventXmlDataObject> = listOf(PodXImageEventXmlDataObject()),
    val podxWeb: List<PodXWebEventXmlDataObject> = listOf(PodXWebEventXmlDataObject()),
    val podxSupport: List<PodXSupportEventXmlDataObject> = listOf(PodXSupportEventXmlDataObject()),
    val podxSocialLink: List<PodXSocialLinkEventXmlDataObject> = listOf(PodXSocialLinkEventXmlDataObject()),
    val podXText: List<PodXTextEventXmlDataObject> = listOf(PodXTextEventXmlDataObject()),
    val podXFeedLink: List<PodXFeedLinkEventXmlDataObject> = listOf(PodXFeedLinkEventXmlDataObject()),
    val podXCallPrompt: List<PodXCallPromptEventXmlDataObject> = listOf(PodXCallPromptEventXmlDataObject()),
    val podXNewsletterSignup: List<PodXNewsletterSignupEventXmlDataObject> = listOf(PodXNewsletterSignupEventXmlDataObject()),
    val podXFeedBack: List<PodXFeedbackEventXmlDataObject> = listOf(PodXFeedbackEventXmlDataObject()),
    val podXPoll: List<PodXPollEventXmlDataObject> = listOf(PodXPollEventXmlDataObject())
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
                "guid" to ValueContainer(""),
                "podx:image" to ValueContainer(listOf(PodXImageEventXmlDataObject())),
                "podx:web" to ValueContainer(listOf(PodXWebEventXmlDataObject())),
                "podx:support" to ValueContainer(listOf(PodXSupportEventXmlDataObject())),
                "podx:socialPrompt" to ValueContainer(listOf(PodXSocialLinkEventXmlDataObject())),
                "podx:text" to ValueContainer(listOf(PodXTextEventXmlDataObject())),
                "podx:feedLink" to ValueContainer(listOf(PodXFeedLinkEventXmlDataObject())),
                "podx:callPrompt" to ValueContainer(listOf(PodXCallPromptEventXmlDataObject())),
                "podx:newsletterSignup" to ValueContainer(listOf(PodXNewsletterSignupEventXmlDataObject())),
                "podx:feedback" to ValueContainer(listOf(PodXFeedbackEventXmlDataObject())),
                "podx:poll" to ValueContainer(listOf(PodXPollEventXmlDataObject()))
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
                guid = xmlParserElementMap["guid"]?.value as String,
                podxImages = xmlParserElementMap["podx:image"]?.value as List<PodXImageEventXmlDataObject>,
                podxWeb = xmlParserElementMap["podx:web"]?.value as List<PodXWebEventXmlDataObject>,
                podxSupport = xmlParserElementMap["podx:support"]?.value as List<PodXSupportEventXmlDataObject>,
                podxSocialLink = xmlParserElementMap["podx:socialPrompt"]?.value as List<PodXSocialLinkEventXmlDataObject>,
                podXText = xmlParserElementMap["podx:text"]?.value as List<PodXTextEventXmlDataObject>,
                podXFeedLink = xmlParserElementMap["podx:feedLink"]?.value as List<PodXFeedLinkEventXmlDataObject>,
                podXCallPrompt = xmlParserElementMap["podx:callPrompt"]?.value as List<PodXCallPromptEventXmlDataObject>,
                podXNewsletterSignup = xmlParserElementMap["podx:newsletterSignup"]?.value as List<PodXNewsletterSignupEventXmlDataObject>,
                podXFeedBack = xmlParserElementMap["podx:feedback"]?.value as List<PodXFeedbackEventXmlDataObject>,
                podXPoll = xmlParserElementMap["podx:poll"]?.value as List<PodXPollEventXmlDataObject>
            )
        }
    }
}
package com.guardian.core.podxevent

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@Parcelize
data class OGMetadata (
    val OGImage: String,
    val OGTitle: String,
    val OGUrl: String,
    val OGType: String
): Parcelable {
    companion object OGMetadataFactory {
        @Throws(IllegalArgumentException::class)
        fun extractOGMetadataFromUrlString (urlString: String): OGMetadata {
            val document: Document = Jsoup.connect(urlString)
                .get()

            val argMap = mutableMapOf<String, String>()

            //select only og meta elements
            document.select("meta[property]")
                .forEach { element ->
                    if (element.attributes()["property"] in listOf(
                        "og:image",
                        "og:title",
                        "og:type",
                        "og:url"
                        )) {
                        argMap[element.attributes()["property"]] = element.attributes()["content"]
                    }
                }

            return OGMetadata(
                OGImage = argMap["og:image"]!!,
                OGTitle = argMap["og:title"] ?: "",
                OGType = argMap["og:type"] ?: "",
                OGUrl = argMap["og:url"] ?: ""
            )
        }
    }
}
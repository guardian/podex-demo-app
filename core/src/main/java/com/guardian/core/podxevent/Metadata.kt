package com.guardian.core.podxevent

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@Parcelize
data class Metadata(
    val OGImage: String,
    val OGTitle: String,
    val OGUrl: String,
    val OGType: String
) : Parcelable {
    companion object OGMetadataFactory {
        @Throws(IllegalArgumentException::class)
        fun extractMetadataFromUrlString(urlString: String): Metadata {
            val document: Document = Jsoup.connect(urlString)
                .get()

            val argMap = mutableMapOf<String, String>()

            // select only og meta elements
            document.select("meta[property]")
                .forEach { element ->
                    if (element.attributes()["property"] in listOf(
                            "og:image",
                            "og:title",
                            "og:type",
                            "og:url"
                        )
                    ) {
                        argMap[element.attributes()["property"]] = element.attributes()["content"]

                        if (!element.attributes()["href"].isNullOrBlank()) {
                            if (element.attributes()["href"][0] == '/') {
                                argMap[element.attributes()["rel"]] =
                                    element.attributes()["abs:href"]
                            } else {
                                argMap[element.attributes()["rel"]] = element.attributes()["href"]
                            }
                        }
                    }
                }

            if (argMap["og:image"].isNullOrBlank() && argMap["apple-touch-icon"].isNullOrBlank()) {
                throw IllegalArgumentException()
            }

            return Metadata(
                OGImage = argMap["og:image"] ?: argMap["apple-touch-icon"] ?: "",
                OGTitle = argMap["og:title"] ?: "",
                OGType = argMap["og:type"] ?: "",
                OGUrl = argMap["og:url"] ?: ""
            )
        }
    }
}

package com.guardian.podxdemo.presentation.podxeventscontainer

import android.view.View
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXWebEvent

data class PodXEventThumbnailData(
    val imageUrlString: String,
    val captionString: String,
    val onClickListener: View.OnClickListener
)

fun PodXWebEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener):
    PodXEventThumbnailData {
    return PodXEventThumbnailData(
        imageUrlString = this.ogMetadata.OGImage,
        captionString = this.caption,
        onClickListener = onClickListener
    )
}

fun PodXImageEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener):
    PodXEventThumbnailData =
    PodXEventThumbnailData(
        imageUrlString = this.urlString,
        captionString = this.caption,
        onClickListener = onClickListener
    )
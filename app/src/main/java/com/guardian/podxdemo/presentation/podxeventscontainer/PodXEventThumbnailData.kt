package com.guardian.podxdemo.presentation.podxeventscontainer

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import com.guardian.core.feed.Feed
import com.guardian.core.feed.FeedRepository
import com.guardian.core.podxevent.PodXCallPromptEvent
import com.guardian.core.podxevent.PodXFeedBackEvent
import com.guardian.core.podxevent.PodXFeedLinkEvent
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXNewsLetterSignUpEvent
import com.guardian.core.podxevent.PodXPollEvent
import com.guardian.core.podxevent.PodXSocialPromptEvent
import com.guardian.core.podxevent.PodXSupportEvent
import com.guardian.core.podxevent.PodXTextEvent
import com.guardian.core.podxevent.PodXWebEvent
import com.guardian.podxdemo.R

data class PodXEventThumbnailData(
    val imageUrlString: String?,
    val imageDrawable: Drawable?,
    val captionString: String,
    val onClickListener: View.OnClickListener
)

fun PodXWebEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener):
    PodXEventThumbnailData {
    return PodXEventThumbnailData(
        imageUrlString = this.ogMetadata.OGImage,
        captionString = this.caption,
        onClickListener = onClickListener,
        imageDrawable = null
    )
}

fun PodXImageEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener):
    PodXEventThumbnailData =
    PodXEventThumbnailData(
        imageUrlString = this.urlString,
        captionString = this.caption,
        onClickListener = onClickListener,
        imageDrawable = null
    )

fun PodXSupportEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener):
    PodXEventThumbnailData =
    PodXEventThumbnailData(
        imageUrlString = this.ogMetadata.OGImage,
        captionString = this.caption,
        onClickListener = onClickListener,
        imageDrawable = null
    )

fun PodXCallPromptEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener,
                                             resources: Resources, theme: Resources.Theme):
    PodXEventThumbnailData =
    PodXEventThumbnailData(
        imageUrlString = null,
        captionString = this.caption,
        onClickListener = onClickListener,
        imageDrawable = resources.getDrawable(R.drawable.baseline_call_black_24, theme)
    )

fun PodXFeedBackEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener):
    PodXEventThumbnailData =
    PodXEventThumbnailData(
        imageUrlString = this.ogMetadata.OGImage,
        captionString = this.caption,
        onClickListener = onClickListener,
        imageDrawable = null
    )

//todo whatever needs to be done here
// fun PodXFeedLinkEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener,
//                                            feed: Feed):
//     PodXEventThumbnailData =
//     PodXEventThumbnailData(
//
//     )

fun PodXNewsLetterSignUpEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener):
    PodXEventThumbnailData {
    return PodXEventThumbnailData(
        imageUrlString = this.ogMetadata.OGImage,
        captionString = this.caption,
        onClickListener = onClickListener,
        imageDrawable = null
    )
}

fun PodXPollEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener):
    PodXEventThumbnailData {
    return PodXEventThumbnailData(
        imageUrlString = this.ogMetadata.OGImage,
        captionString = this.caption,
        onClickListener = onClickListener,
        imageDrawable = null
    )
}

fun PodXSocialPromptEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener):
    PodXEventThumbnailData {
    return PodXEventThumbnailData(
        imageUrlString = this.ogMetadata.OGImage,
        captionString = this.caption,
        onClickListener = onClickListener,
        imageDrawable = null
    )
}

fun PodXTextEvent.toPodXEventThumbnail(onClickListener: View.OnClickListener,
                                       resources: Resources, theme: Resources.Theme):
    PodXEventThumbnailData {
    return PodXEventThumbnailData(
        imageUrlString = null,
        captionString = this.caption,
        onClickListener = onClickListener,
        imageDrawable = resources.getDrawable(R.drawable.baseline_library_books_black_24, theme)
    )
}


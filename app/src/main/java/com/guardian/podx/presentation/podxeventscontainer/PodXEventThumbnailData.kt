package com.guardian.podx.presentation.podxeventscontainer

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
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
import com.guardian.podx.R

data class PodXEventThumbnailData(
    val imageUrlString: String?,
    val imageDrawable: Drawable?,
    val badgeDrawable: Drawable?,
    val notificationString: String,
    val captionString: String,
    val onClickListener: View.OnClickListener,
    val imageSwitch: Boolean = false
)

fun PodXWebEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_link, theme)
        )
    }

fun PodXImageEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData =
        PodXEventThumbnailData(
            imageUrlString = this.urlString,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_image, theme),
            imageSwitch = true
        )

fun PodXSupportEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData =
        PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_link, theme)
        )

fun PodXCallPromptEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData =
        PodXEventThumbnailData(
            imageUrlString = null,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = resources.getDrawable(R.drawable.baseline_call_black_24, theme),
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_call, theme)
        )

fun PodXFeedBackEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData =
        PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_feedback, theme)
        )

fun PodXFeedLinkEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = this.remoteFeedImageUrlString,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_podcast, theme)
        )
    }

fun PodXNewsLetterSignUpEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_newsletter, theme)
        )
    }

fun PodXPollEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_poll, theme)
        )
    }

fun PodXSocialPromptEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_social, theme)
        )
    }

fun PodXTextEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = null,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = resources.getDrawable(R.drawable.baseline_library_books_black_24, theme),
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_article, theme)
        )
    }

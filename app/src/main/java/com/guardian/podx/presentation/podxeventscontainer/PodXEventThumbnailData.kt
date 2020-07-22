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
    val imageSwitch: Boolean = false,
    val newEventCallback: () -> Unit,
    val uniqueEventId: String
)

fun PodXWebEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme,
    newEventCallback: () -> Unit
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_link, theme),
            newEventCallback = newEventCallback,
            uniqueEventId = "web${this.id}"
        )
    }

fun PodXImageEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme,
    newEventCallback: () -> Unit
):
    PodXEventThumbnailData =
        PodXEventThumbnailData(
            imageUrlString = this.urlString,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_image, theme),
            imageSwitch = true,
            newEventCallback = newEventCallback,
            uniqueEventId = "image${this.id}"
        )

fun PodXSupportEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme,
    newEventCallback: () -> Unit
):
    PodXEventThumbnailData =
        PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_link, theme),
            newEventCallback = newEventCallback,
            uniqueEventId = "support${this.id}"
        )

fun PodXCallPromptEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme,
    newEventCallback: () -> Unit
):
    PodXEventThumbnailData =
        PodXEventThumbnailData(
            imageUrlString = null,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = resources.getDrawable(R.drawable.baseline_call_black_24, theme),
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_call, theme),
            newEventCallback = newEventCallback,
            uniqueEventId = "call${this.id}"
        )

fun PodXFeedBackEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme,
    newEventCallback: () -> Unit
):
    PodXEventThumbnailData =
        PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_feedback, theme),
            newEventCallback = newEventCallback,
            uniqueEventId = "feedback${this.id}"
        )

fun PodXFeedLinkEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme,
    newEventCallback: () -> Unit
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = this.remoteFeedImageUrlString,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_podcast, theme),
            newEventCallback = newEventCallback,
            uniqueEventId = "feedlink${this.id}"
        )
    }

fun PodXNewsLetterSignUpEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme,
    newEventCallback: () -> Unit
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_newsletter, theme),
            newEventCallback = newEventCallback,
            uniqueEventId = "newslettersignup${this.id}"
        )
    }

fun PodXPollEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme,
    newEventCallback: () -> Unit
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_poll, theme),
            newEventCallback = newEventCallback,
            uniqueEventId = "poll${this.id}"
        )
    }

fun PodXSocialPromptEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme,
    newEventCallback: () -> Unit
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = this.ogMetadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_social, theme),
            newEventCallback = newEventCallback,
            uniqueEventId = "socialprompt${this.id}"
        )
    }

fun PodXTextEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme,
    newEventCallback: () -> Unit
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = null,
            captionString = this.caption,
            notificationString = this.notification,
            onClickListener = onClickListener,
            imageDrawable = resources.getDrawable(R.drawable.baseline_library_books_black_24, theme),
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_article, theme),
            newEventCallback = newEventCallback,
            uniqueEventId = "text${this.id}"
        )
    }

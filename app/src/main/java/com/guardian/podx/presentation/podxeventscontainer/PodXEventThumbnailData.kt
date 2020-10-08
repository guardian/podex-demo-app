package com.guardian.podx.presentation.podxeventscontainer

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.MutableLiveData
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
import com.guardian.podx.utils.toTimestampMSS

data class PodXEventThumbnailData(
    val imageUrlString: String?,
    val imageDrawable: Drawable?,
    val badgeDrawable: Drawable?,
    val notificationString: String,
    val captionString: String,
    val timeStart: Long,
    val timeStampsActive: String,
    val onClickListener: View.OnClickListener,
    val imageSwitch: Boolean = false,
    val uniqueEventId: String,
    val expandSwitch: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
)

fun PodXWebEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData {
        return PodXEventThumbnailData(
            imageUrlString = this.metadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            timeStart = this.timeStart,
            timeStampsActive = "${this.timeStart.toTimestampMSS(resources)} - ${
            this.timeEnd.toTimestampMSS(
                resources
            )
            }",
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_link, theme),
            uniqueEventId = this.getThumbnailId()
        )
    }

fun PodXWebEvent.getThumbnailId(): String = "web${this.id}"

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
            timeStart = this.timeStart,
            timeStampsActive = "${this.timeStart.toTimestampMSS(resources)} - ${this.timeEnd.toTimestampMSS(resources)}",
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_image, theme),
            imageSwitch = true,
            uniqueEventId = this.getThumbnailId()
        )

fun PodXImageEvent.getThumbnailId(): String = "image${this.id}"

fun PodXSupportEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData =
        PodXEventThumbnailData(
            imageUrlString = this.metadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            timeStart = this.timeStart,
            timeStampsActive = "${this.timeStart.toTimestampMSS(resources)} - ${this.timeEnd.toTimestampMSS(resources)}",
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_link, theme),
            uniqueEventId = this.getThumbnailId()
        )

fun PodXSupportEvent.getThumbnailId(): String = "support${this.id}"

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
            timeStart = this.timeStart,
            timeStampsActive = "${this.timeStart.toTimestampMSS(resources)} - ${this.timeEnd.toTimestampMSS(resources)}",
            onClickListener = onClickListener,
            imageDrawable = resources.getDrawable(R.drawable.baseline_call_black_24, theme),
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_call, theme),
            uniqueEventId = this.getThumbnailId()
        )

fun PodXCallPromptEvent.getThumbnailId(): String = "call${this.id}"

fun PodXFeedBackEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData =
        PodXEventThumbnailData(
            imageUrlString = this.metadata.OGImage,
            captionString = this.caption,
            notificationString = this.notification,
            timeStart = this.timeStart,
            timeStampsActive = "${this.timeStart.toTimestampMSS(resources)} - ${this.timeEnd.toTimestampMSS(resources)}",
            onClickListener = onClickListener,
            imageDrawable = null,
            badgeDrawable = resources.getDrawable(R.drawable.ic_icons_feedback, theme),
            uniqueEventId = this.getThumbnailId()
        )

fun PodXFeedBackEvent.getThumbnailId(): String = "feedback${this.id}"

fun PodXFeedLinkEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData = PodXEventThumbnailData(
        imageUrlString = this.remoteFeedImageUrlString,
        captionString = this.caption,
        notificationString = this.notification,
        timeStart = this.timeStart,
        timeStampsActive = "${this.timeStart.toTimestampMSS(resources)} - ${this.timeEnd.toTimestampMSS(resources)}",
        onClickListener = onClickListener,
        imageDrawable = null,
        badgeDrawable = resources.getDrawable(R.drawable.ic_icons_podcast, theme),
        uniqueEventId = this.getThumbnailId()
    )

fun PodXFeedLinkEvent.getThumbnailId(): String = "feedlink${this.id}"

fun PodXNewsLetterSignUpEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData = PodXEventThumbnailData(
        imageUrlString = this.metadata.OGImage,
        captionString = this.caption,
        notificationString = this.notification,
        timeStart = this.timeStart,
        timeStampsActive = "${this.timeStart.toTimestampMSS(resources)} - ${this.timeEnd.toTimestampMSS(resources)}",
        onClickListener = onClickListener,
        imageDrawable = null,
        badgeDrawable = resources.getDrawable(R.drawable.ic_icons_newsletter, theme),
        uniqueEventId = this.getThumbnailId()
    )

fun PodXNewsLetterSignUpEvent.getThumbnailId(): String = "newsletter${this.id}"

fun PodXPollEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData = PodXEventThumbnailData(
        imageUrlString = this.metadata.OGImage,
        captionString = this.caption,
        notificationString = this.notification,
        timeStart = this.timeStart,
        timeStampsActive = "${this.timeStart.toTimestampMSS(resources)} - ${this.timeEnd.toTimestampMSS(resources)}",
        onClickListener = onClickListener,
        imageDrawable = null,
        badgeDrawable = resources.getDrawable(R.drawable.ic_icons_poll, theme),
        uniqueEventId = this.getThumbnailId()
    )

fun PodXPollEvent.getThumbnailId(): String = "poll${this.id}"

fun PodXSocialPromptEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData = PodXEventThumbnailData(
        imageUrlString = this.metadata.OGImage,
        captionString = this.caption,
        notificationString = this.notification,
        timeStart = this.timeStart,
        timeStampsActive = "${this.timeStart.toTimestampMSS(resources)} - ${this.timeEnd.toTimestampMSS(resources)}",
        onClickListener = onClickListener,
        imageDrawable = null,
        badgeDrawable = resources.getDrawable(R.drawable.ic_icons_social, theme),
        uniqueEventId = this.getThumbnailId()
    )

fun PodXSocialPromptEvent.getThumbnailId(): String = "socialprompt${this.id}"

fun PodXTextEvent.toPodXEventThumbnail(
    onClickListener: View.OnClickListener,
    resources: Resources,
    theme: Resources.Theme
):
    PodXEventThumbnailData = PodXEventThumbnailData(
        imageUrlString = null,
        captionString = this.caption,
        notificationString = this.notification,
        timeStart = this.timeStart,
        timeStampsActive = "${this.timeStart.toTimestampMSS(resources)} - ${this.timeEnd.toTimestampMSS(resources)}",
        onClickListener = onClickListener,
        imageDrawable = resources.getDrawable(R.drawable.baseline_library_books_black_24, theme),
        badgeDrawable = resources.getDrawable(R.drawable.ic_icons_article, theme),
        uniqueEventId = this.getThumbnailId()
    )

fun PodXTextEvent.getThumbnailId(): String = "text${this.id}"

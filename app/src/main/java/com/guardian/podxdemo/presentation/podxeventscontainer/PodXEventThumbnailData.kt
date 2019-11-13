package com.guardian.podxdemo.presentation.podxeventscontainer

import android.view.View

data class PodXEventThumbnailData(
    val imageUrlString: String,
    val captionString: String,
    val onClickListener: View.OnClickListener
)
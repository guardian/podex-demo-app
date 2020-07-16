package com.guardian.podx.presentation.common.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.guardian.podx.utils.toFeedDisplayTime

@BindingAdapter("timeStampMs")
fun bindTimestamp(textView: TextView, timestamp: Long?) {
    textView.text = timestamp?.toFeedDisplayTime()
}
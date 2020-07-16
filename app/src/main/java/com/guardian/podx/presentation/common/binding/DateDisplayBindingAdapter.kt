package com.guardian.podx.presentation.common.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.Date

@BindingAdapter("displayDate")
fun bind(textView: TextView, date: Date?) {
    val format = SimpleDateFormat("MMMM dd yyyy")
    if (date != null) {
        textView.text = format.format(date)
    } else {
        textView.text = ""
    }
}
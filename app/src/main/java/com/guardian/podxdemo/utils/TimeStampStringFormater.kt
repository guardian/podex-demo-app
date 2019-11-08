package com.guardian.podxdemo.utils

import android.content.Context
import com.guardian.podxdemo.R
import kotlin.math.floor

/**
 * Utility method to convert milliseconds to a display of minutes and seconds
 *
 * Taken from UAMP and  converted to extension function
 */
fun Long.toTimestampMSS(context: Context): String {
    val totalSeconds = floor(this / 1E3).toInt()
    val minutes = totalSeconds / 60
    val remainingSeconds = totalSeconds - (minutes * 60)
    return if (this < 0) context.getString(R.string.duration_unknown)
    else context.getString(R.string.duration_format).format(minutes, remainingSeconds)
}
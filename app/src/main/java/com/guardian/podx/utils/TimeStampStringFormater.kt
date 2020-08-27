package com.guardian.podx.utils

import android.content.Context
import android.content.res.Resources
import com.guardian.podx.R
import kotlin.math.floor

/**
 * Utility method to convert milliseconds to a display of minutes and seconds
 *
 * Taken from UAMP and  converted to extension function
 */
fun Long.toTimestampMSS(resources: Resources): String {
    val totalSeconds = floor(this / 1E3).toInt()
    val minutes = totalSeconds / 60
    val remainingSeconds = totalSeconds - (minutes * 60)
    return if (this < 0) resources.getString(R.string.duration_unknown)
    else resources.getString(R.string.duration_format).format(minutes, remainingSeconds)
}

/**
 * Utility method to convert milliseconds to a display rounded minutes or seconds
 */
fun Long.toFeedDisplayTime(): String {
    val minutes = floor(this / 1E3).toInt() / 60
    val seconds = floor(this / 1E3).toInt() - (minutes * 60)
    return if (minutes <= 0) {
        "%ds".format(seconds)
    } else {
        "%dm".format(minutes)
    }
}

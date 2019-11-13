package com.guardian.core.library

/**
 * Extension function to
 */
fun String.parseNormalPlayTimeToMillis(): Long {
    trim().split(Regex(":"), 3)
        .apply {
            return when (size) {
                1 -> {
                    // expect the format is SS.mmm
                    (this[0].toDouble() * 1000).toLong()
                }
                2 -> {
                    // expect the format MM.SS.mmm
                    (this[0].toLong() * 60000 + (this[1].toDouble() * 1000)).toLong()
                }

                3 -> {
                    // expect the format HH:MM:SS.mmm
                    (this[0].toLong() * 3600000 + this[1].toLong() * 60000 +
                        (this[2].toDouble() * 1000)).toLong()
                }

                else -> {
                    throw IllegalArgumentException("Invalid NPT time stamp")
                }
            }
        }
}

fun String.parseNormalPlayTimeToMillisOrNull(): Long? {
    return try {
        parseNormalPlayTimeToMillis()
    } catch (e: Exception) {
        null
    }
}
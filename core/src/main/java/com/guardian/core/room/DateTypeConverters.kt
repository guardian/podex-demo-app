package com.guardian.core.room

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverters {
    @TypeConverter
    fun toDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun toLong(value: Date): Long {
        return value.getTime()
    }
}
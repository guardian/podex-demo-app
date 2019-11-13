package com.guardian.core.room

import androidx.room.TypeConverter
import java.util.Date
import java.util.concurrent.atomic.AtomicBoolean

class RoomTypeConverters {
    @TypeConverter
    fun longToDate(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToLong(value: Date): Long = value.getTime()

    @TypeConverter
    fun atomicBooleanToBoolean(value: AtomicBoolean): Boolean = value.get()

    @TypeConverter
    fun booleanToAtomicBoolean(value: Boolean): AtomicBoolean = AtomicBoolean(value)
}
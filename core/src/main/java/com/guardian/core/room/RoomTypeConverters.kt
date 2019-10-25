package com.guardian.core.room

import androidx.room.TypeConverter
import com.guardian.core.podxevent.PodxType
import java.util.Date

class RoomTypeConverters {
    @TypeConverter
    fun stringToPodXType(string: String): PodxType = PodxType.valueOf(string)

    @TypeConverter
    fun podxTypeToString(podxType: PodxType): String = podxType.toString()

    @TypeConverter
    fun longToDate(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToLong(value: Date): Long = value.getTime()
}
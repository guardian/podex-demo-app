package com.guardian.core.room

import androidx.room.TypeConverter
import com.guardian.core.podxevent.PodXType
import java.util.Date

class RoomTypeConverters {
    @TypeConverter
    fun stringToPodXType(string: String): PodXType = PodXType.valueOf(string)

    @TypeConverter
    fun podxTypeToString(podXType: PodXType): String = podXType.toString()

    @TypeConverter
    fun longToDate(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToLong(value: Date): Long = value.getTime()
}
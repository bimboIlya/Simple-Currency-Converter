package com.example.cfttesttask.data.sources.db

import androidx.room.TypeConverter
import java.util.*

class DateConverters {

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun timestampToDate(timestamp: Long): Date = Date(timestamp)
}
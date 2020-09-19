package com.example.cfttesttask.data.sources.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cfttesttask.data.Currency

@Database(
    entities = [Currency::class],
    version = 1
)
@TypeConverters(DateConverters::class)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
}
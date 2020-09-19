package com.example.cfttesttask.data.sources.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cfttesttask.data.Currency

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Currency>)

    @Query("SELECT * FROM Currency")
    suspend fun getCurrencyList(): List<Currency>

    @Query("SELECT * FROM Currency")
    fun observeCurrencyList(): LiveData<List<Currency>>

    @Query("DELETE FROM Currency")
    suspend fun deleteTable()
}

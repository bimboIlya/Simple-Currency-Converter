package com.example.cfttesttask.di

import android.app.Application
import androidx.room.Room
import com.example.cfttesttask.data.sources.api.BASE_URL
import com.example.cfttesttask.data.sources.api.CurrencyApi
import com.example.cfttesttask.data.sources.db.CurrencyDao
import com.example.cfttesttask.data.sources.db.CurrencyDatabase

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun getService(): CurrencyApi {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        return retrofit.create(CurrencyApi::class.java)
    }

    @Singleton
    @Provides
    fun getDb(context: Application): CurrencyDatabase {
        return Room.databaseBuilder(context, CurrencyDatabase::class.java, "currency.db").build()
    }

    @Singleton
    @Provides
    fun getDao(db: CurrencyDatabase): CurrencyDao = db.currencyDao()
}
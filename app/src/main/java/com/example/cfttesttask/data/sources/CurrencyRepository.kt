package com.example.cfttesttask.data.sources

import com.example.cfttesttask.data.sources.api.ApiResponseItem
import com.example.cfttesttask.data.Currency
import com.example.cfttesttask.data.sources.api.CurrencyApi
import com.example.cfttesttask.data.sources.db.CurrencyDao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val api: CurrencyApi,
    private val dao: CurrencyDao
) {

    /**
     * @return currency list from db if it's not null or older than 24h. Returns currency list
     * from network if previous conditions are false or we explicitly say so
     */
    suspend fun updateDataIfNecessary(forceUpdate: Boolean): List<Currency> =
        withContext(Dispatchers.IO) {
            var data = dao.getCurrencyList()
            val shouldUpdate = data.isNullOrEmpty() || forceUpdate ||
                    data[0].isOlderThan24Hours() // DANGEROUS! might throw IndexOutOfBounds exception if order changes

            if (shouldUpdate) {
                try {
                    val response = api.getCurrencyList()
                    if (response.isSuccessful) {
                        val mappedData = mapData(response.body()!!)  // never null if successful
                        data = mappedData
                        dao.insertAll(mappedData)
                    }
                } catch (e: Throwable) {
                    if (e !is CancellationException) {
                        throw DataUpdateException("Failed to update currency", e)
                    }
                }
            }

            return@withContext data
        }

    private fun mapData(response: ApiResponseItem): List<Currency> {
        val currencyList = mutableListOf<Currency>()
        val valuteMap = response.valutes

        valuteMap.forEach { (_, value) ->
            currencyList.add(Currency(response.date, value))
        }

        return currencyList
    }
}

class DataUpdateException(message: String, cause: Throwable? = null) : Throwable(message, cause)
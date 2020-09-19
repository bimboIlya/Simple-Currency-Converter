package com.example.cfttesttask.data.sources.api

import retrofit2.Response
import retrofit2.http.GET

const val BASE_URL = "https://www.cbr-xml-daily.ru/"

interface CurrencyApi {
    @GET("daily_json.js")
    suspend fun getCurrencyList(): Response<ApiResponseItem>
}
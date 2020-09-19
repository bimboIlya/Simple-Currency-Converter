package com.example.cfttesttask.data.sources.api

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ApiResponseItem(
    @SerializedName("Date") val date: Date,
    @SerializedName("PreviousDate") val previousDate: Date,
    @SerializedName("PreviousURL") val previousUrl: String,
    @SerializedName("Timestamp") val timestamp: Date,
    @SerializedName("Valute") val valutes: Map<String, Valute>
)

data class Valute(
    @SerializedName("ID") val id: String,
    @SerializedName("NumCode") val numCode: Long,
    @SerializedName("CharCode") val charCode: String,
    @SerializedName("Nominal") val nominal: Long,
    @SerializedName("Name") val name: String,
    @SerializedName("Value") val value: Double,
    @SerializedName("Previous") val previousValue: Double
)

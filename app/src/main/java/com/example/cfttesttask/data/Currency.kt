package com.example.cfttesttask.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cfttesttask.data.sources.api.Valute
import java.util.Date
import java.util.Calendar

@Entity
data class Currency(
    val date: Date,
    @PrimaryKey val id: String,
    val numCode: Long,
    val charCode: String,
    val foreignAmount: Double,
    val name: String,
    val rubAmount: Double,

    /**
     * Cost of **1** of foreign currency in rubles
     */
    val rubValue: Double,

    val previousRubValue: Double
) {

    constructor(date: Date, valute: Valute) :
            this(
                date,
                valute.id,
                valute.numCode,
                valute.charCode,
                valute.nominal.toDouble(),
                nameToNominateCase(valute.name),
                valute.value,
                valute.value / valute.nominal,
                valute.previousValue
            )

    fun isOlderThan24Hours(): Boolean {
        val calendar = Calendar.getInstance().apply {
            time = date
            add(Calendar.DAY_OF_YEAR, 1)
        }
        val nextDate = calendar.time

        return Date() > nextDate
    }

    companion object {
        fun nameToNominateCase(name: String): String =
            when (name) {
                "Армянских драмов" -> "Армянские драмы"
                "Венгерских форинтов" -> "Венгерские форинты"
                "Гонконгских долларов" -> "Гонкогские доллары"
                "Индийских рупий" -> "Индийские рупии"
                "Казахстанских тенге" -> "Казахстанские тенге"
                "Киргизских сомов" -> "Киргизские сомы"
                "Молдавских леев" -> "Молдавские левы"
                "Норвежских крон" -> "Норвежские кроны"
                "Турецких лир" -> "Турецкие лиры"
                "Узбекских сумов" -> "Узбекские сумы"
                "Украинских гривен" -> "Украинские гривны"
                "Шведских крон" -> "Шведские кроны"
                "Южноафриканских рэндов" -> "Южноафриканские рэнды"
                "Вон Республики Корея" -> "Воны Республики Корея"
                "Японских иен" -> "Японские иены"
                else -> name
            }

        fun convertFromRubles(amount: Double, currency: Currency): Double =
            amount / currency.rubValue

        fun convertToRubles(amount: Double, currency: Currency): Double =
            amount * currency.rubValue
    }
}
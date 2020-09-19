package com.example.cfttesttask.data

import com.example.cfttesttask.data.Currency
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class CurrencyTest {

    @Test
    fun whenCurrentItemIsOlderThan24Hours() {
        val date = Date(Date().time - 2*24*60*60*1000) // 2 days in the past
        val subject = getCurrency(date)
        assertTrue(subject.isOlderThan24Hours())
    }

    @Test
    fun whenCurrentItemIsNotOlder24Hours() {
        val date = Date(Date().time + 1000) // 1 s in the future
        val subject = getCurrency(date)
        assertFalse(subject.isOlderThan24Hours())
    }
}

private fun getCurrency(date: Date): Currency =
    Currency(date, "1", 1, "1", 1.0, "1", 1.0, 1.0, 1.0)

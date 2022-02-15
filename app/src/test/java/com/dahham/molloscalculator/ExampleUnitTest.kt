package com.dahham.molloscalculator

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun getFixerCountries(){
        val endpoints = FixerEndpointsService.getInstance().getSymbols()
        assertEquals(endpoints?.symbols?.isNotEmpty(), true)
    }

    @Test
    fun convertCurrency(){
        val endpoints = FixerEndpointsService.getInstance().convertCurrency("GBP", "JPY", 25.0f)
        assertEquals(endpoints?.success, true)
    }
}
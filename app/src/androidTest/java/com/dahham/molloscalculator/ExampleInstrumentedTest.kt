package com.dahham.molloscalculator

import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.dahham.molloscalculator", appContext.packageName)
    }

    @Test
    fun getCountriesFromViewModel(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val viewModelProvider = ViewModelProvider.AndroidViewModelFactory.getInstance(ApplicationProvider.getApplicationContext())
        viewModelProvider.create(MainActivityViewModel::class.java).run {
            val countries = getCountries(appContext)
            countries.toString()
        }
    }
}
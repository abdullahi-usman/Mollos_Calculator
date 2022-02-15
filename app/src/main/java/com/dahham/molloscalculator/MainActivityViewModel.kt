package com.dahham.molloscalculator

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dahham.molloscalculator.database.CountriesDatabase
import com.dahham.molloscalculator.database.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel: ViewModel() {

    private val countries: MutableLiveData<List<Country>> = MutableLiveData()
    private val fixerEndpointsService = FixerEndpointsService.getInstance()
    private lateinit var countriesDatabase: CountriesDatabase
    var fromCountry = MutableLiveData<Country>().apply { this.value = Country("AED", "United Arab Emirates Dirham") }
    var toCountry = MutableLiveData<Country>().apply { this.value = Country("AED", "United Arab Emirates Dirham"); }

    /*

    After we got our list of countries from server we cache it using Room,
    and then on subsequent calls, we will get from database.
     */
    fun getCountries(context: Context): LiveData<List<Country>>{

        if (countries.value.isNullOrEmpty()) {
            countriesDatabase = CountriesDatabase.getInstance(context)
            viewModelScope.launch(Dispatchers.Default) {

                countriesDatabase.getDao().getAll().let {
                    if (it.isNotEmpty()) {
                        countries.postValue(it.toList())
                    }else{
                            val countriesList = fixerEndpointsService.getSymbols()?.symbols
                            countriesList?.let {
                                countries.postValue(countriesList.toList())
                                countriesDatabase.getDao().putAll(*countriesList)
                            }

                    }
                }
            }
        }

        return countries
    }


    fun convertCurrency(amount: Float, callback: (answer: Float) -> Unit){
        viewModelScope.launch(Dispatchers.Default) {

                fixerEndpointsService.convertCurrency(fromCountry.value?.code!!,
                    toCountry.value?.code!!,
                    amount)?.let {
                    withContext(Dispatchers.Main) {
                        callback(it.result)
                    }
                }
            
        }
    }
}
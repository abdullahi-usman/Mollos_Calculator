package com.dahham.molloscalculator

import com.dahham.molloscalculator.database.Country
import com.dahham.molloscalculator.database.CountryTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.nio.file.attribute.AclEntry.newBuilder

interface FixerEndpoints {

    @GET("symbols")
    fun getSupportedSymbols(@QueryMap options: Map<String, String>): Call<FixerSupportedSymbols>


    @GET("convert")
    fun convertCurrency(@QueryMap options: Map<String, String>): Call<FixerConvertResult>
}


class FixerEndpointsService private constructor(){

    private val gson = GsonBuilder().registerTypeAdapter(Array<Country>::class.java, CountryTypeAdapter()).create()
    private val fixerEndpoints = Retrofit.Builder().baseUrl("http://data.fixer.io/api/")
        .addConverterFactory(GsonConverterFactory.create(gson)).build().create(FixerEndpoints::class.java)
    private val queryOptions = mapOf(Pair("access_key", "3561995f178cc171c0d6145706d226ff"))

    companion object{
        @Volatile
        private var INSTANCE: FixerEndpointsService? = null

        fun getInstance() = INSTANCE ?: synchronized(this){
            INSTANCE ?: FixerEndpointsService()
        }
    }

    fun getSymbols(): FixerSupportedSymbols?{
        return fixerEndpoints.getSupportedSymbols(queryOptions).execute().body()
    }


    fun convertCurrency(from: String, to: String, amount: Float): FixerConvertResult?{
        return fixerEndpoints.convertCurrency(queryOptions.run {
            plus(arrayOf(Pair("from", from), Pair("to", to), Pair("amount", amount.toString())))
        }).execute().also {
            val error = it.errorBody()
            it.toString()
        } .body()
    }

}
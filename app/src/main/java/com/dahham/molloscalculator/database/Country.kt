package com.dahham.molloscalculator.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


@Entity
data class Country(@PrimaryKey(autoGenerate = false) val code: String, val name: String)

class CountryTypeAdapter: TypeAdapter<Array<Country>>(){
    override fun write(out: JsonWriter?, value: Array<Country>?) {

    }

    override fun read(`in`: JsonReader?): Array<Country> {

        val countries = arrayListOf<Country>()
        `in`?.let {
            `in`.beginObject()
            while (`in`.hasNext()) {
                countries.add(Country(`in`.nextName(), `in`.nextString()))
            }
            `in`.endObject()
        }



        return countries.toTypedArray()
    }


}
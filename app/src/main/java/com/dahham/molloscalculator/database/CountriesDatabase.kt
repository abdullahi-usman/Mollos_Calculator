package com.dahham.molloscalculator.database

import android.content.Context
import androidx.room.*

@Dao
interface CountriesDao {

    @Query("SELECT * from country ORDER BY code ASC")
    fun getAll(): Array<Country>

    @Insert
    fun putAll(vararg countries: Country);

}

@Database(entities = [Country::class], version = 1)
abstract class CountriesDatabase : RoomDatabase() {

    abstract fun getDao(): CountriesDao

    companion object {
        @Volatile
        private var INSTANCE: CountriesDatabase? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context, CountriesDatabase::class.java, "countries.db")
                .allowMainThreadQueries().build()
        }
    }
}
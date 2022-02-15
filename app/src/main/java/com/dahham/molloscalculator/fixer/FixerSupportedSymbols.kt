package com.dahham.molloscalculator

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dahham.molloscalculator.database.Country
import java.util.*


data class FixerSupportedSymbols(val success: Boolean, val id: String, val symbols: Array<Country>)


/*
*
* {
*    "success": true,
*    "query": {
*        "from": "GBP",
*        "to": "JPY",
*        "amount": 25
*    },     
*    "info": {
*        "timestamp": 1519328414,
*        "rate": 148.972231
*    },
*    "historical": ""
*    "date": "2018-02-22"
*    "result": 3724.305775
* }
*
*/

class FixerConvertResult(val success: Boolean, val query: Query, val info: Info, val historical: String, val date: String, val result: Float){
    data class Query(val from: String, val to: String, val amount: String)
    data class Info(val timestamp: Long, val rate: Float)
}
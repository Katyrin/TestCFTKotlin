package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import retrofit2.Call
import retrofit2.http.GET

interface CbrApi {
    @GET("daily_json.js")
    fun getCurrencies(

    ): Call<CurrenciesDTO>
}
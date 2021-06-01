package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import io.reactivex.Single
import retrofit2.http.GET

interface CbrApi {
    @GET("daily_json.js")
    fun getCurrencies(): Single<CurrenciesDTO>
}
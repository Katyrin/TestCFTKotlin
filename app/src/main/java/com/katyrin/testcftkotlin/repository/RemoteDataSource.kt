package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import retrofit2.Callback

interface RemoteDataSource {
    fun getCurrencyDetails(callback: Callback<CurrenciesDTO>)
}
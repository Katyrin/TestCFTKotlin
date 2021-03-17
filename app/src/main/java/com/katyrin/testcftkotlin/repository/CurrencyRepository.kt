package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import retrofit2.Callback

interface CurrencyRepository {
    fun getCurrenciesFromServer(callback: Callback<CurrenciesDTO>)
}
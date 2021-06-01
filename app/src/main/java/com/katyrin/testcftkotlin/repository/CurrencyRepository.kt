package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import io.reactivex.Single

interface CurrencyRepository {
    fun getCurrenciesFromServer(): Single<CurrenciesDTO>
}
package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import io.reactivex.Single

interface RemoteDataSource {
    fun getCurrencyDetails(): Single<CurrenciesDTO>
}
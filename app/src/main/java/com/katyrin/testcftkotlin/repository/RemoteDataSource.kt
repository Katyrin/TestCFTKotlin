package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import io.reactivex.rxjava3.core.Single

interface RemoteDataSource {
    fun getCurrencyDetails(): Single<CurrenciesDTO>
}
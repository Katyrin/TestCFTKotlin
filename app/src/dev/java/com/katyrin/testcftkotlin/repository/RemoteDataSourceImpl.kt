package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RemoteDataSourceImpl @Inject constructor(
    private val mockCurrencyApi: MockCbrApi
) : RemoteDataSource {
    override fun getCurrencyDetails(): Single<CurrenciesDTO> =
        mockCurrencyApi.getCurrencies()
            .subscribeOn(Schedulers.io())
}
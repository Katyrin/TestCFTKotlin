package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


class RemoteDataSourceImpl @Inject constructor(
    private val currencyApi: CbrApi
) : RemoteDataSource {
    override fun getCurrencyDetails(): Single<CurrenciesDTO> = currencyApi.getCurrencies()
}
package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import retrofit2.Callback
import javax.inject.Inject


class RemoteDataSource @Inject constructor(
    private val currencyApi: CbrApi
) : RemoteDataSourceInterface {
    override fun getCurrencyDetails(callback: Callback<CurrenciesDTO>) {
        currencyApi.getCurrencies().enqueue(callback)
    }
}
package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.BASE_URL
import com.katyrin.testcftkotlin.model.CurrenciesDTO
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior


class RemoteDataSource {

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).build()
    private val behavior = NetworkBehavior.create()
    private val mockRetrofit = MockRetrofit.Builder(retrofit).networkBehavior(behavior).build()
    private val delegate: BehaviorDelegate<CbrApi> = mockRetrofit.create(CbrApi::class.java)
    private val mockCurrencyApi = MockCbrApi(delegate)

    fun getCurrencyDetails(callback: Callback<CurrenciesDTO>) {
        mockCurrencyApi.getCurrencies().enqueue(callback)
    }
}
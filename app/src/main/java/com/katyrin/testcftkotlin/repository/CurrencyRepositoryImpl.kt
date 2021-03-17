package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import retrofit2.Callback

class CurrencyRepositoryImpl(private val remoteDataSource: RemoteDataSource): CurrencyRepository {
    override fun getCurrenciesFromServer(callback: Callback<CurrenciesDTO>) {
        remoteDataSource.getCurrencyDetails(callback)
    }

}
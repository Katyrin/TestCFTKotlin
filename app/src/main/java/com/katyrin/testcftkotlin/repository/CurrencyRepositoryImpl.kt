package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import io.reactivex.Single
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : CurrencyRepository {
    override fun getCurrenciesFromServer(): Single<CurrenciesDTO> =
        remoteDataSource.getCurrencyDetails()
}
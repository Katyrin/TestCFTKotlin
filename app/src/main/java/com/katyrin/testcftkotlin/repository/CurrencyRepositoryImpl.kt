package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.CurrenciesDTO
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val remoteDataSourceImpl: RemoteDataSourceImpl
) : CurrencyRepository {
    override fun getCurrenciesFromServer(): Single<CurrenciesDTO> =
        remoteDataSourceImpl.getCurrencyDetails()
}
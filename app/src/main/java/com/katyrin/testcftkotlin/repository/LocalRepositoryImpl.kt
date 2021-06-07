package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.Currency
import com.katyrin.testcftkotlin.model.room.CurrencyEntity
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : LocalRepository {

    override fun getAllCurrencies(): Single<List<Currency>> =
        localDataSource.getCurrenciesDao().all()
            .subscribeOn(Schedulers.io())
            .map(::convertCurrencyEntityToCurrency)


    override fun insertEntity(currency: Currency): Completable =
        Completable
            .fromAction {
                localDataSource.getCurrenciesDao().insert(convertCurrencyToEntity(currency))
            }
            .subscribeOn(Schedulers.io())

    private fun convertCurrencyEntityToCurrency(entityList: List<CurrencyEntity>): List<Currency> =
        entityList.map {
            Currency(it.id, it.numCode, it.charCode, it.nominal, it.name, it.value, it.previous)
        }

    private fun convertCurrencyToEntity(currency: Currency): CurrencyEntity =
        CurrencyEntity(
            currency.id,
            currency.numCode,
            currency.charCode,
            currency.nominal,
            currency.name,
            currency.value,
            currency.previous
        )
}
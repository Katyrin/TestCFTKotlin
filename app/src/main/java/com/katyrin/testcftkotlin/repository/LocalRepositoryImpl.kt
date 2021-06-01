package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.Currency
import com.katyrin.testcftkotlin.model.room.CurrenciesDao
import com.katyrin.testcftkotlin.model.room.CurrencyEntity
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: CurrenciesDao,
    private val uiScheduler: Scheduler
) : LocalRepository {

    override fun getAllCurrencies(): Single<List<Currency>> =
        localDataSource.all()
            .subscribeOn(Schedulers.io())
            .flatMap { currencyEntityList ->
                Single.create { emitter ->
                    emitter.onSuccess(convertCurrencyEntityToCurrency(currencyEntityList))
                }
            }


    override fun insertEntity(currency: Currency) {
        Completable
            .fromAction { localDataSource.insert(convertCurrencyToEntity(currency)) }
            .subscribeOn(Schedulers.io())
            .observeOn(uiScheduler)
            .subscribe()
    }

    private fun convertCurrencyEntityToCurrency(entityList: List<CurrencyEntity>): List<Currency> {
        return entityList.map {
            Currency(it.id, it.numCode, it.charCode, it.nominal, it.name, it.value, it.previous)
        }
    }

    private fun convertCurrencyToEntity(currency: Currency): CurrencyEntity {
        return CurrencyEntity(
            currency.id, currency.numCode, currency.charCode, currency.nominal,
            currency.name, currency.value, currency.previous
        )
    }
}
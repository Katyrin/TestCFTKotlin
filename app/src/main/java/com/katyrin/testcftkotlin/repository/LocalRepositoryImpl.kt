package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.Currency
import com.katyrin.testcftkotlin.model.room.CurrenciesDao
import com.katyrin.testcftkotlin.model.room.CurrencyEntity
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: CurrenciesDao
) : LocalRepository {
    override fun getAllCurrencies(): List<Currency> {
        return convertCurrencyEntityToCurrency(localDataSource.all())
    }

    override fun saveEntity(currency: Currency) {
        localDataSource.insert(convertCurrencyToEntity(currency))
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
package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.Currency
import io.reactivex.Single

interface LocalRepository {
    fun getAllCurrencies(): Single<List<Currency>>
    fun insertEntity(currency: Currency)
}
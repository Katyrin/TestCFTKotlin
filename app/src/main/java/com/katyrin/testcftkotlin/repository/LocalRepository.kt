package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.Currency

interface LocalRepository {
    fun getAllCurrencies(): List<Currency>
    fun saveEntity(currency: Currency)
}
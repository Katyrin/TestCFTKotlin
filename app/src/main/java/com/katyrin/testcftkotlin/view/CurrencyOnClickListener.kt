package com.katyrin.testcftkotlin.view

import com.katyrin.testcftkotlin.model.Currency

interface CurrencyOnClickListener {
    fun onCurrencyClicked(currency: Currency)
}
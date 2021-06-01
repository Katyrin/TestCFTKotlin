package com.katyrin.testcftkotlin.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.katyrin.testcftkotlin.model.CurrenciesDTO
import com.katyrin.testcftkotlin.model.Currency
import com.katyrin.testcftkotlin.model.ValuteDTO

fun convertCurrenciesDTOToModel(currenciesDTO: CurrenciesDTO): List<Currency> {
    val currency: Collection<ValuteDTO> = currenciesDTO.valute!!.values
    val list: MutableList<Currency> = mutableListOf()
    currency.forEach {
        list.add(
            Currency(
                it.id ?: "null", it.numCode ?: 0,
                it.charCode ?: "null", it.nominal ?: 0, it.name ?: "null",
                it.value ?: 0.0, it.previous ?: 0.0
            )
        )
    }
    return list
}

fun View.createAndShow(
    text: String, actionText: String = "",
    action: ((View) -> Unit)? = null,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length)
        .also {
            if (action != null) it.setAction(actionText, action)
        }.show()
}

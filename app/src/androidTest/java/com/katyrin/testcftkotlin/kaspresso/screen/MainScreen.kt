package com.katyrin.testcftkotlin.kaspresso.screen

import android.view.View
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.katyrin.testcftkotlin.R
import org.hamcrest.Matcher

object MainScreen : Screen<MainScreen>() {

    val currenciesRecyclerView = KRecyclerView(
        builder = { withId(R.id.currenciesRecyclerView) },
        itemTypeBuilder = { itemType(::CurrencyItem) }
    )

    class CurrencyItem(parent: Matcher<View>) : KRecyclerItem<CurrencyItem>(parent) {

        val currencyName = KTextView(parent) { withId(R.id.currencyName) }
        val currencyCharCode = KTextView(parent) { withId(R.id.currencyCharCode) }
        val currencyNominal = KTextView(parent) { withId(R.id.currencyNominal) }
        val currencyValue = KTextView(parent) { withId(R.id.currencyValue) }
    }
}
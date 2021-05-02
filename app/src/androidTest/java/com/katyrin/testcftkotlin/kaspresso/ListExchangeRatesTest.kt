package com.katyrin.testcftkotlin.kaspresso


import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.agoda.kakao.recycler.RecyclerActions
import com.agoda.kakao.text.KTextView
import com.katyrin.testcftkotlin.kaspresso.screen.DialogScreen
import com.katyrin.testcftkotlin.kaspresso.screen.MainScreen
import com.katyrin.testcftkotlin.view.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ListExchangeRatesTest: KTestCase() {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkListExchangeRates() {

        run {
            step("Check currency content") {
                checkCurrencies(
                    Currency("Австралийский доллар (36)", "AUD", "1", "58.1546"),
                    Currency("Азербайджанский манат (944)", "AZN", "1", "44.0524"),
                    Currency("Армянских драмов (51)", "AMD", "100", "14.3761"),
                    Currency("Белорусский рубль (933)", "BYN", "1", "29.2398"),
                    Currency("Болгарский лев (975)", "BGN", "1", "46.295")
                )
            }
        }
    }

    class Currency(
        val currencyName: String,
        val currencyCharCode: String,
        val currencyNominal: String,
        val currencyValue: String,
    )

    private fun checkCurrencies(vararg currencies: Currency) {
        currencies.forEachIndexed { index, currency ->
            MainScreen {
                currenciesRecyclerView {
                    childAt<MainScreen.CurrencyItem>(index) {
                        currencyName {
                            isDisplayed()
                            hasText(currency.currencyName)
                        }
                        currencyCharCode {
                            isDisplayed()
                            hasText(currency.currencyCharCode)
                        }
                        currencyNominal {
                            isDisplayed()
                            hasText(currency.currencyNominal)
                        }
                        currencyValue {
                            isDisplayed()
                            hasText(currency.currencyValue)
                        }
                    }
                }
            }
        }
    }

    @Test
    fun checkConversion() {
        var currencyUSD = 0.0
        var nominal = 0
        before {

        }
        run {
            step("Scroll to USD item") {
                MainScreen {
                    currenciesRecyclerView {
                        scrollToPosition(10)
                    }
                }
            }
            step("Click USD") {
                MainScreen {
                    currenciesRecyclerView {
                        childAt<MainScreen.CurrencyItem>(10) {
                            currencyCharCode {
                                hasText("USD")
                            }
                            currencyValue {
                                currencyUSD = getText().toDouble()
                            }
                            currencyNominal {
                                nominal = getText().toInt()
                            }
                            click()
                        }
                    }
                }
            }
            step("Check currency conversion") {
                DialogScreen {
                    amountInRubles {
                        typeText("1000")
                    }
                    closeSoftKeyboard()
                    positiveButtonDialog {
                        click()
                    }
                    result {
                        hasText(countResult1000USD(nominal, currencyUSD))
                    }
                }
            }
        }
    }

    private fun countResult1000USD(nominal: Int, currencyUSD: Double): String {
        val result = 1000 * nominal / currencyUSD
        return String.format("%.2f", result) + " USD"
    }


    private fun RecyclerActions.scrollToPosition(position: Int) {
        view.perform(object : ViewAction {
            override fun getDescription() = "Scroll RecyclerView to $position"

            override fun getConstraints() = ViewMatchers.isAssignableFrom(RecyclerView::class.java)

            override fun perform(controller: UiController, view: View) {
                if (view is RecyclerView) {
                    if (position < view.adapter!!.itemCount) {
                        view.scrollToPosition(position)
                        controller.loopMainThreadUntilIdle()
                        val lastView = view.findViewHolderForLayoutPosition(position)!!.itemView
                        view.scrollBy(0, lastView.height)
                        controller.loopMainThreadUntilIdle()
                    } else {
                        throw IndexOutOfBoundsException("$position position is not exist")
                    }
                }
            }
        })
    }

    private fun KTextView.getText() : String {
        var text = ""
        view.perform(object : ViewAction {
            override fun getConstraints() = ViewMatchers.isAssignableFrom(TextView::class.java)

            override fun getDescription(): String = "Get text"

            override fun perform(uiController: UiController?, view: View?) {
                text = (view as TextView).text.toString()
            }
        })
        return text
    }
}
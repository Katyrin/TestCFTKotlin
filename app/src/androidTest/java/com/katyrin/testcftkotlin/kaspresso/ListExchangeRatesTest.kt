package com.katyrin.testcftkotlin.kaspresso


import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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
            step("Check currency AUD") {
                MainScreen {
                    currenciesRecyclerView {
                        childAt<MainScreen.CurrencyItem>(0) {
                            currencyName {
                                isDisplayed()
                                hasText("Австралийский доллар (36)")
                            }
                            currencyCharCode {
                                isDisplayed()
                                hasText("AUD")
                            }
                            currencyNominal {
                                isDisplayed()
                                hasText("1")
                            }
                            currencyValue {
                                isDisplayed()
                                hasText("58.1546")
                            }
                        }
                    }
                }
            }
            step("Check currency AZN") {
                MainScreen {
                    currenciesRecyclerView {
                        childAt<MainScreen.CurrencyItem>(1) {
                            currencyName {
                                isDisplayed()
                                hasText("Азербайджанский манат (944)")
                            }
                            currencyCharCode {
                                isDisplayed()
                                hasText("AZN")
                            }
                            currencyNominal {
                                isDisplayed()
                                hasText("1")
                            }
                            currencyValue {
                                isDisplayed()
                                hasText("44.0524")
                            }
                        }
                    }
                }
            }
            step("Check currency AMD") {
                MainScreen {
                    currenciesRecyclerView {
                        childAt<MainScreen.CurrencyItem>(2) {
                            currencyName {
                                isDisplayed()
                                hasText("Армянских драмов (51)")
                            }
                            currencyCharCode {
                                isDisplayed()
                                hasText("AMD")
                            }
                            currencyNominal {
                                isDisplayed()
                                hasText("100")
                            }
                            currencyValue {
                                isDisplayed()
                                hasText("14.3761")
                            }
                        }
                    }
                }
            }
            step("Check currency BYN") {
                MainScreen {
                    currenciesRecyclerView {
                        childAt<MainScreen.CurrencyItem>(3) {
                            currencyName {
                                isDisplayed()
                                hasText("Белорусский рубль (933)")
                            }
                            currencyCharCode {
                                isDisplayed()
                                hasText("BYN")
                            }
                            currencyNominal {
                                isDisplayed()
                                hasText("1")
                            }
                            currencyValue {
                                isDisplayed()
                                hasText("29.2398")
                            }
                        }
                    }
                }
            }
            step("Check currency BGN") {
                MainScreen {
                    currenciesRecyclerView {
                        childAt<MainScreen.CurrencyItem>(4) {
                            currencyName {
                                isDisplayed()
                                hasText("Болгарский лев (975)")
                            }
                            currencyCharCode {
                                isDisplayed()
                                hasText("BGN")
                            }
                            currencyNominal {
                                isDisplayed()
                                hasText("1")
                            }
                            currencyValue {
                                isDisplayed()
                                hasText("46.295")
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun checkConversion() {
        run {
            step("Click USD") {
                MainScreen {
                    currenciesRecyclerView {
                        childAt<MainScreen.CurrencyItem>(10) {
                            currencyCharCode {
                                hasText("USD")
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
                        hasText("13.36 USD")
                    }
                }
            }
        }
    }
}
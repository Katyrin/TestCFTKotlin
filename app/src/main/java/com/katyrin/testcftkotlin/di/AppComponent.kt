package com.katyrin.testcftkotlin.di

import com.katyrin.testcftkotlin.view.CurrenciesFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [PresentationModule::class, AppModule::class])
@Singleton
interface AppComponent {
    fun inject(currenciesFragment: CurrenciesFragment)
}
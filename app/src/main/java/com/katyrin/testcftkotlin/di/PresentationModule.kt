package com.katyrin.testcftkotlin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.katyrin.testcftkotlin.repository.CurrencyRepository
import com.katyrin.testcftkotlin.repository.CurrencyRepositoryImpl
import com.katyrin.testcftkotlin.repository.LocalRepository
import com.katyrin.testcftkotlin.repository.LocalRepositoryImpl
import com.katyrin.testcftkotlin.viewmodel.CurrenciesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
interface PresentationModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CurrenciesViewModel::class)
    fun bindMainViewModel(viewModel: CurrenciesViewModel): ViewModel

    @Binds
    @Singleton
    fun currencyRemoteRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    @Singleton
    fun currencyLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository
}
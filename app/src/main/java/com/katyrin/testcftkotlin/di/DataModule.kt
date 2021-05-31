package com.katyrin.testcftkotlin.di

import com.katyrin.testcftkotlin.repository.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    @Singleton
    fun currencyRemoteRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    @Singleton
    fun currencyLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    @Binds
    @Singleton
    fun remoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSourceImpl
}
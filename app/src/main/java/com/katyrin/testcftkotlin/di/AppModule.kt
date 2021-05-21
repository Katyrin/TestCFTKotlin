package com.katyrin.testcftkotlin.di

import com.katyrin.testcftkotlin.App
import com.katyrin.testcftkotlin.model.room.CurrenciesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    fun app(): App {
        return app
    }

    @Provides
    @Singleton
    fun localDataSource(app: App): CurrenciesDao = app.getCurrenciesDao()
}
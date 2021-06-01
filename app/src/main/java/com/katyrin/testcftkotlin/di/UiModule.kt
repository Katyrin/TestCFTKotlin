package com.katyrin.testcftkotlin.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Singleton

@Module
class UiModule {

    @Provides
    @Singleton
    fun uiScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
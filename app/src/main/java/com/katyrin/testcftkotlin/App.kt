package com.katyrin.testcftkotlin

import android.app.Application
import com.katyrin.testcftkotlin.di.AppComponent
import com.katyrin.testcftkotlin.di.AppModule
import com.katyrin.testcftkotlin.di.DaggerAppComponent


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()
        appInstance = this
    }

    companion object {
        private lateinit var appInstance: App
    }
}
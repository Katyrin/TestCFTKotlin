package com.katyrin.testcftkotlin.di

import com.google.gson.GsonBuilder
import com.katyrin.testcftkotlin.utils.BASE_URL
import com.katyrin.testcftkotlin.repository.CbrApi
import com.katyrin.testcftkotlin.repository.CurrencyApiInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
interface ApiModule {

    companion object {

        @Provides
        @Singleton
        fun currencyApi(client: OkHttpClient): CbrApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(client)
            .build().create(CbrApi::class.java)

        @Provides
        @Singleton
        fun client(interceptor: Interceptor): OkHttpClient =
            OkHttpClient.Builder().apply {
                addInterceptor(interceptor)
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }.build()
    }

    @Binds
    @Singleton
    fun interceptor(currencyApiInterceptor: CurrencyApiInterceptor): Interceptor
}
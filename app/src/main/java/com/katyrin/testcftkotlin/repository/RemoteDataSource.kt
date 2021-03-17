package com.katyrin.testcftkotlin.repository

import com.google.gson.GsonBuilder
import com.katyrin.testcftkotlin.model.BASE_URL
import com.katyrin.testcftkotlin.model.CurrenciesDTO
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RemoteDataSource {

    private val currencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .client(createOkHttpClient(CurrencyApiInterceptor()))
        .build().create(CbrApi::class.java)

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(interceptor)
        okHttpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return okHttpClient.build()
    }

    fun getCurrencyDetails(callback: Callback<CurrenciesDTO>) {
        currencyApi.getCurrencies().enqueue(callback)
    }

    inner class CurrencyApiInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            return chain.proceed(chain.request())
        }
    }
}
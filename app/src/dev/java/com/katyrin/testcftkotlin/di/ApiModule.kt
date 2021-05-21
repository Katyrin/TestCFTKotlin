package com.katyrin.testcftkotlin.di

import com.katyrin.testcftkotlin.model.BASE_URL
import com.katyrin.testcftkotlin.repository.CbrApi
import com.katyrin.testcftkotlin.repository.MockCbrApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun retrofit(): Retrofit = Retrofit.Builder().baseUrl(BASE_URL).build()

    @Provides
    @Singleton
    fun behavior(): NetworkBehavior = NetworkBehavior.create()

    @Provides
    @Singleton
    fun mockRetrofit(retrofit: Retrofit, behavior: NetworkBehavior): MockRetrofit =
        MockRetrofit.Builder(retrofit).networkBehavior(behavior).build()

    @Provides
    @Singleton
    fun delegate(mockRetrofit: MockRetrofit): BehaviorDelegate<CbrApi> =
        mockRetrofit.create(CbrApi::class.java)

    @Provides
    @Singleton
    fun mockCurrencyApi(delegate: BehaviorDelegate<CbrApi>): MockCbrApi = MockCbrApi(delegate)


}
package com.example.retrsample2.di

import android.util.Log
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitProviderModule {
    @RxProvider
    @Provides fun getRxRetrofit(@URL URL: String): Retrofit= Retrofit.Builder()
        .baseUrl(URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @CorProvider
    @Provides fun getCorRetrofit(@URL URL: String): Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
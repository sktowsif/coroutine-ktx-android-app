package com.example.profiles.di

import com.example.profiles.BuildConfig
import com.example.profiles.data.UserAPI
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val remoteModule = module {
    single { createWebService<UserAPI>(BuildConfig.BASE_API_URL) }
}

inline fun <reified T> createWebService(url: String): T {
    val okHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())

    return retrofit.build().create(T::class.java)
}
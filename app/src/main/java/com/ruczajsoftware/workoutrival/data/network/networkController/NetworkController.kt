package com.ruczajsoftware.workoutrival.data.network.networkController

import com.ruczajsoftware.workoutrival.data.network.ServicesApi
import com.ruczajsoftware.workoutrival.data.network.util.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "http://159.65.93.215:8080/"

object NetworkController {
    operator fun invoke(): ServicesApi {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(ServicesApi::class.java)
    }
}
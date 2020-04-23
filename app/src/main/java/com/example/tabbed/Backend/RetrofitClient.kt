package com.example.tabbed.Backend

import android.util.Base64
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor


object RetrofitClient {
    private val AUTH = "Basic " + Base64.encodeToString("admin:1234".toByteArray(), Base64.NO_WRAP)
    const val BASE_URL = "http://192.168.1.37/FoodApiREST/public/"

    private val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpLoggingInterceptor.redactHeader("Authorization")
            httpLoggingInterceptor.redactHeader("Cookie")
        }
    }

    //private val gson: GsonBuilder().serializeNulls().create()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", AUTH)
                .method(original.method, original.body)
            val request = requestBuilder.build()

            chain.proceed(request)

        }.build()

    private val okHttpClientWithLog = okHttpClient.newBuilder().addInterceptor(interceptor).build()

    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientWithLog)
            .build()

        retrofit.create(Api::class.java)
    }
}
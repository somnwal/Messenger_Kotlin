package com.messenger.main.retrofit

import android.annotation.SuppressLint
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        @SuppressLint("AuthLeak")
        private const val BASE_URL = "http://129.154.55.103:5000/"
        var retrofit: Retrofit =
            Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}
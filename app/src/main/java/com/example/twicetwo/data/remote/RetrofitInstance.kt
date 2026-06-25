package com.example.twicetwo.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: TwiceToApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.0.21:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TwiceToApi::class.java)
    }
}
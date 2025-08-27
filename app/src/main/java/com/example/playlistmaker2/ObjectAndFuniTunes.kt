package com.example.playlistmaker2

import com.example.playlistmaker2.RetrofitITunes.retrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

object RetrofitITunes {
    private const val BASE_URL = "https://itunes.apple.com"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiSearch = retrofit.create(MusicApi::class.java)
}


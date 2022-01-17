package com.annhienktuit.muzikplayer.retrofit

import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit


object RetrofitClient {
    private var retrofit: Retrofit? = null
    val getVideoService: GetVideoService
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://61e52105595afe00176e5333.mockapi.io")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(GetVideoService::class.java)
        }
}
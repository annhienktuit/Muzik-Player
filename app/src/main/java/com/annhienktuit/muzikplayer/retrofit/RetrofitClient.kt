package com.annhienktuit.muzikplayer.retrofit

import android.content.Context
import com.annhienktuit.muzikplayer.utils.MuzikUtils
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.io.File


class RetrofitClient(context: Context, baseURL: String) {
    private val BASE_URL = baseURL
    private var retrofit: Retrofit? = null
    private val cacheSize = 5 * 1024 * 1024
    private val retroCacheDir = File(context.cacheDir, "retrofit")
    private val retroCache = Cache(retroCacheDir, cacheSize.toLong())
    private val okHttpClient = OkHttpClient.Builder()
        .cache(retroCache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (MuzikUtils.isInternetAvailable(context))
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build() // 7 days
            chain.proceed(request)
        }.build()

    fun getVideoService():GetVideoService{
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofit!!.create(GetVideoService::class.java)
    }

    fun getOnlineTrackService():GetTrackService{
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofit!!.create(GetTrackService::class.java)
    }

    fun getChartService():GetChartService{
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofit!!.create(GetChartService::class.java)
    }
}
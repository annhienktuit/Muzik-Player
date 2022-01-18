package com.annhienktuit.muzikplayer.retrofit

import android.content.Context
import com.annhienktuit.muzikplayer.utils.MuzikUtils
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit
import java.io.File


class RetrofitClient(context: Context) {
    private var retrofit: Retrofit? = null
    val cacheSize = 5 * 1024 * 1024
    val retroCacheDir = File(context.cacheDir, "retrofit")
    val retroCache = Cache(retroCacheDir, cacheSize.toLong())
    val okHttpClient = OkHttpClient.Builder()
        .cache(retroCache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (MuzikUtils.haveInternetConnection(context))
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
            chain.proceed(request)
        }.build()

    fun getVideoService():GetVideoService{
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://61e52105595afe00176e5333.mockapi.io")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofit!!.create(GetVideoService::class.java)
    }
    //    val getVideoService: GetVideoService
//        get() {
//            if (retrofit == null) {
//                retrofit = Retrofit.Builder()
//                    .baseUrl("https://61e52105595afe00176e5333.mockapi.io")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//            }
//            return retrofit!!.create(GetVideoService::class.java)
//        }
}
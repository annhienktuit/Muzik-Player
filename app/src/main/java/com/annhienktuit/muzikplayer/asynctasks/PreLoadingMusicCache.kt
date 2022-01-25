package com.annhienktuit.muzikplayer.asynctasks

import android.annotation.SuppressLint
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.annhienktuit.muzikplayer.models.PreCacheParams
import com.annhienktuit.muzikplayer.utils.CacheUtils.Companion.simpleMusicCache
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter

class PreLoadingMusicCache: AsyncTask<PreCacheParams, Void, Void>() {
    @SuppressLint("StaticFieldLeak")
    private lateinit var uri: Uri
    private lateinit var id: String
    private val TAG = "Pre-cache"
    override fun doInBackground(vararg params: PreCacheParams?): Void? {
        try {
            uri = Uri.parse(params[0]!!.url)
            id = params[0]!!.id
            val dataSpec: DataSpec = DataSpec.Builder()
                .setUri(uri)
                .setFlags(DataSpec.FLAG_ALLOW_CACHE_FRAGMENTATION)
                .build()
            val upstreamDataSource: HttpDataSource =
                DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
                    .createDataSource()
            val dataSink: DataSink = CacheDataSink(simpleMusicCache, 5 * 1024 * 1024)
            val cacheDataSource = CacheDataSource(
                simpleMusicCache,
                upstreamDataSource,
                TeeDataSource(upstreamDataSource, dataSink),
                dataSink,
                0,
                null,
                null
            )
            cacheVideo(dataSpec,cacheDataSource)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    private fun cacheVideo(dataSpec: DataSpec, cacheDataSource: CacheDataSource) {
        val listener =
            CacheWriter.ProgressListener { requestLength, bytesCached, newBytesCached ->
                Log.i(
                    TAG,
                    "Bytes downloaded $bytesCached"
                )
            }
        val cacheWriter = CacheWriter(cacheDataSource, dataSpec, null, null)
        cacheWriter.cache()
    }
}
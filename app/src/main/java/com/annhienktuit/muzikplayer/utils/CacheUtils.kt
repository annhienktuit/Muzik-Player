package com.annhienktuit.muzikplayer.utils

import android.app.Application
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

class CacheUtils: Application() {
    companion object{
        lateinit var simpleCache: SimpleCache
        lateinit var simpleMusicCache: SimpleCache
        const val exoPlayerCacheSize:Long = 200 * 1024 * 1024
        lateinit var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor
        lateinit var exoDatabaseProvider: StandaloneDatabaseProvider
    }

    override fun onCreate() {
        super.onCreate()
        leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize)
        exoDatabaseProvider = StandaloneDatabaseProvider(this)
        val videoCacheDirectory = File(cacheDir.toString(), "video")
        val musicCacheDirectory = File(cacheDir.toString(),"music")
        simpleCache = SimpleCache(videoCacheDirectory , leastRecentlyUsedCacheEvictor, exoDatabaseProvider)
        simpleMusicCache = SimpleCache(musicCacheDirectory , leastRecentlyUsedCacheEvictor, exoDatabaseProvider)
    }
}
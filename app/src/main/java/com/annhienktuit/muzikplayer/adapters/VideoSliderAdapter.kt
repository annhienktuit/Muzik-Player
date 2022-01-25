package com.annhienktuit.muzikplayer.adapters

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.annhienktuit.muzikplayer.fragments.SingleVideoFragment
import com.annhienktuit.muzikplayer.utils.CacheUtils.Companion.simpleCache
import com.annhienktuit.muzikplayer.utils.MuzikUtils.isInternetAvailable
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import java.util.*

class VideoSliderAdapter(
    context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    mediaList: ArrayList<MediaItem>,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    val mContext = context
    val TAG = "VideoPlayerFragment"
    val mediaList = mediaList
    override fun createFragment(position: Int): Fragment {
        if (position + 1 < mediaList.size && isInternetAvailable(mContext)) {
            doPreCacheVideo(position + 1)
            if (position - 1 >= 0 && isInternetAvailable(mContext)) {
                doPreCacheVideo(position - 1)
            }
        } else {
            Log.e(TAG, "Cannot perform pre-cache on position $position")
        }
        return SingleVideoFragment(mediaList[position])
    }

    private fun handleSuccess() {
        Log.i("Nhiennha ", "Pre-Cache Video success")
    }

    private fun handleError(throwable: Throwable) {
        Log.e("Nhiennha ", throwable.message!!)
    }

    private fun doPreCacheVideo(position: Int) {
        Thread {
            val dataSpec: DataSpec = DataSpec.Builder()
                .setUri(mediaList[position].localConfiguration!!.uri)
                .setLength(2048000)
                .setFlags(DataSpec.FLAG_ALLOW_CACHE_FRAGMENTATION)
                .build()
            val upstreamDataSource: HttpDataSource =
                DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
                    .createDataSource()
            val dataSink: DataSink =
                CacheDataSink(simpleCache, 5 * 1024 * 1024) // 1 video precache 1 fragment
            val cacheDataSource = CacheDataSource(
                simpleCache,
                upstreamDataSource,
                TeeDataSource(upstreamDataSource, dataSink),
                dataSink,
                0,
                null,
                null
            )
            cacheVideo(dataSpec, cacheDataSource)
        }.start()
    }

    private fun cacheVideo(dataSpec: DataSpec, cacheDataSource: CacheDataSource) {
        val cacheWriter = CacheWriter(cacheDataSource, dataSpec, null, null)
        cacheWriter.cache()
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

}
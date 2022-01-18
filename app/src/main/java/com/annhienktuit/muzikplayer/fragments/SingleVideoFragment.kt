package com.annhienktuit.muzikplayer.fragments

import android.content.Context
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.utils.CacheUtils
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache


class SingleVideoFragment : Fragment {
    private var mediaItem: MediaItem? = null
    var playerView: PlayerView? = null
    var exoPlayer: ExoPlayer? = null
    var simpleCache: SimpleCache = CacheUtils.simpleCache
    lateinit var httpDataSourceFactory: HttpDataSource.Factory
    lateinit var defaultDataSourceFactory: DefaultDataSource.Factory
    lateinit var cacheDataSourceFactory: DataSource.Factory
    private val loadControl: LoadControl = DefaultLoadControl.Builder()
        .setBufferDurationsMs(1024, 128 * 1024, 1024, 1024)
        .build()
    private val TAG = "VideoPlayerFragment" + this.id
    constructor(mediaItem: MediaItem){
        this.mediaItem = mediaItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_single_video, container, false)
        val context = view.context
        playerView = view.findViewById(R.id.videoSliderView)
        prepareDataSource(context)
        createNewPlayerInstance(context)
        preparePlayer(mediaItem)
        startPlayer()
        return view
    }

    override fun onPause() {
        //pausePlayer()
        releasePlayer()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        context?.let { createNewPlayerInstance(it) }
        preparePlayer(mediaItem)
        startPlayer()
    }

    private fun createNewPlayerInstance(context: Context) {
        exoPlayer = ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .setLoadControl(loadControl)
            .setTrackSelector(DefaultTrackSelector(context))
            .build()
    }

    private fun preparePlayer(mediaItem: MediaItem?) {
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
            .createMediaSource(mediaItem!!)
        exoPlayer!!.playWhenReady = false
        exoPlayer!!.setMediaSource(mediaSource)
        playerView!!.player = exoPlayer
    }

    private fun startPlayer() {
        Log.i(TAG, "StartPlayer")
        Log.i("Nhiennha ",exoPlayer.toString())
        exoPlayer!!.prepare()
        exoPlayer!!.playWhenReady = true
    }

    private fun prepareDataSource(context: Context) {
        httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
        defaultDataSourceFactory = DefaultDataSource.Factory(context,
            httpDataSourceFactory as DefaultHttpDataSource.Factory
        )
        cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setCacheReadDataSourceFactory(FileDataSource.Factory())
    }

    private fun pausePlayer() {
        Log.i(TAG, "PausePlayer")
        exoPlayer!!.seekTo(1)
        exoPlayer!!.pause()
        exoPlayer!!.playWhenReady = false
    }

    private fun releasePlayer(){
        Log.i(TAG,"ReleasePlayer")
        exoPlayer!!.seekTo(0)
        exoPlayer!!.release()
        exoPlayer = null
    }

}
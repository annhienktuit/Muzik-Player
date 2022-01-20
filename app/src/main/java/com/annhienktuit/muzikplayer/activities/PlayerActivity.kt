package com.annhienktuit.muzikplayer.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.palette.graphics.Palette
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.asynctasks.ConvertUrlToBitmapAsync
import com.annhienktuit.muzikplayer.asynctasks.PreLoadingMusicCache
import com.annhienktuit.muzikplayer.models.PreCacheParams
import com.annhienktuit.muzikplayer.services.MusicService
import com.annhienktuit.muzikplayer.utils.MuzikUtils
import com.annhienktuit.muzikplayer.utils.MuzikUtils.isServiceRunning
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView

class PlayerActivity : AppCompatActivity() {
    private lateinit var playerView: PlayerControlView
    private lateinit var imgArtWork: ImageView
    private lateinit var tvArtist: TextView
    private lateinit var tvSongTitle: TextView
    private lateinit var rlPlayer: RelativeLayout
    private var exoPlayer: ExoPlayer? = null
    private var currentIndexFromService: Int? = null
    private var listID =  ArrayList<String>()
    private var listURL =  ArrayList<String>()
    private var listArtwork = ArrayList<String>()
    private var listTitle = ArrayList<String>()
    private var listArtist = ArrayList<String>()
    private var currentIndex = 0
    private val connection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service is MusicService.PlayerServiceBinder){
                Log.i("Nhiennha ","Service Connected")
                exoPlayer = service.getExoPlayerInstance()
                currentIndexFromService = service.getCurrentIndex()
                preCacheMedia()
                addListener()
                playerView.player = service.getExoPlayerInstance()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#00000000")
        setContentView(R.layout.activity_player)
        getDataFromBundle()
        attachView()
        val intent = Intent(this, MusicService::class.java)
        sendDataToService(intent)
        if(!isServiceRunning("MusicService")){
            startService(intent)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        setPaletteBackground(currentIndex)
        tvSongTitle.text = listTitle[currentIndex]
        tvArtist.text = listArtist[currentIndex]
        Glide.with(applicationContext)
            .load(listArtwork[currentIndex])
            .into(imgArtWork)
    }

    private fun getDataFromBundle() {
        val extras = intent?.extras
        if(extras != null){
            listID = extras.getStringArrayList("listID") as ArrayList<String>
            listURL = extras.getStringArrayList("listURL") as ArrayList<String>
            listArtwork = extras.getStringArrayList("listArtwork") as ArrayList<String>
            listArtist = extras.getStringArrayList("listArtist") as ArrayList<String>
            listTitle = extras.getStringArrayList("listTitle") as ArrayList<String>
            currentIndex = extras.getInt("Index")
            if(currentIndex != currentIndexFromService && currentIndexFromService != null)
                currentIndex = currentIndexFromService!!
        }
    }

    private fun sendDataToService(intent: Intent) {
        intent.putExtra("listID",listID)
        intent.putExtra("listURL",listURL)
        intent.putExtra("listArtwork",listArtwork)
        intent.putExtra("listTitle",listTitle)
        intent.putExtra("listArtist",listArtist)
        intent.putExtra("Index",currentIndex)
    }

    private fun setPaletteBackground(index: Int) {
        if(MuzikUtils.isInternetAvailable(this)){
            val bitmap = ConvertUrlToBitmapAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listArtwork[index]).get()
            val builder = Palette.Builder(bitmap)
            val palette = builder.generate { palette: Palette? ->
                if (palette != null) {
                    palette.dominantSwatch ?.let { rlPlayer.setBackgroundColor(it.rgb) }
                }
            }
        }
    }

    private fun addListener() {
        exoPlayer?.addListener(object: Player.Listener{
            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                if(MuzikUtils.isInternetAvailable(applicationContext)){
                    preCacheMedia()
                    setPaletteBackground(newPosition.mediaItemIndex)
                }
                Glide.with(applicationContext)
                    .load(listArtwork[newPosition.mediaItemIndex])
                    .placeholder(R.drawable.ic_logo)
                    .into(imgArtWork)
                tvArtist.text = listArtist[newPosition.mediaItemIndex]
                tvSongTitle.text = listTitle[newPosition.mediaItemIndex]
            }
        })
    }

    private fun preCacheMedia(){
        val timer = object: CountDownTimer(5000, 1000){
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                doCaching()
            }
        }.start()
    }

    private fun doCaching(){
        var idx = exoPlayer?.currentMediaItemIndex
        if (idx != null) {
            AsyncTask.execute {
                for (i in idx until idx + 5) {
                    if (MuzikUtils.isInternetAvailable(this)) {
                        val params = PreCacheParams(listID[idx], listURL[idx])
                        val cacheTask = PreLoadingMusicCache()
                        cacheTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params)
                        idx++
                        if (idx >= listID.size) break
                    } else {
                        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
                        break
                    }
                }
            }
        }
    }

    private fun attachView() {
        playerView = findViewById(R.id.playerView)
        imgArtWork = findViewById(R.id.imgArtWork)
        tvArtist = findViewById(R.id.tvArtist)
        tvSongTitle = findViewById(R.id.tvSongTitle)
        rlPlayer = findViewById(R.id.rlPlayer)
    }
}
package com.annhienktuit.muzikplayer.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.palette.graphics.Palette
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.asynctasks.ConvertUrlToBitmapAsync
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#00000000")
        setContentView(R.layout.activity_player)
        getDataFromBundle()
        attachView()
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

    private fun setPaletteBackground(index: Int) {
        val bitmap = ConvertUrlToBitmapAsync().execute(listArtwork[index]).get()
        val builder = Palette.Builder(bitmap)
        val palette = builder.generate { palette: Palette? ->
            if (palette != null) {
                palette.dominantSwatch ?.let { rlPlayer.setBackgroundColor(it.rgb) }
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
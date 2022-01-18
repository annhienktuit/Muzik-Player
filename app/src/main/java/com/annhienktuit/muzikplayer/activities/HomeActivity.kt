package com.annhienktuit.muzikplayer.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.fragments.MusicFragment
import com.annhienktuit.muzikplayer.fragments.VideoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private val fragmentMusic: Fragment = MusicFragment()
    private val fragmentVideo: Fragment = VideoFragment()
    private val fm: FragmentManager = supportFragmentManager
    private var activeFragment: Fragment = fragmentMusic

    private lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        attachView()
        setupFragment()
    }

    private fun setupFragment() {
        fm.beginTransaction().add(R.id.fragment_container, fragmentVideo, "Video").hide(fragmentVideo).commit()
        fm.beginTransaction().add(R.id.fragment_container,fragmentMusic, "Music").commit()

        val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_music -> {
                        fm.beginTransaction().hide(activeFragment).show(fragmentMusic).commit()
                        activeFragment = fragmentMusic
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.nav_video -> {
                        fm.beginTransaction().hide(activeFragment).show(fragmentVideo).commit()
                        activeFragment = fragmentVideo
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }
        bottomNav.setOnItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun attachView() {
        bottomNav = findViewById(R.id.bottomNavigationView)
    }
}
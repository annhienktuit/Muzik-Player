package com.annhienktuit.muzikplayer.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.fragments.MusicFragment
import com.annhienktuit.muzikplayer.fragments.VideoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        attachView()
        setupFragment()
    }

    private fun setupFragment() {
        val fragment = when (bottomNav.selectedItemId) {
            R.id.nav_music -> MusicFragment()
            R.id.nav_video -> VideoFragment()
            else -> {
                MusicFragment()
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, fragment)
        transaction.commit()

        val mOnBottomNavigationView = BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_music -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, MusicFragment())
                    transaction.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_video -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, VideoFragment())
                    transaction.commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        bottomNav.setOnItemSelectedListener(mOnBottomNavigationView)
    }

    private fun attachView() {
        bottomNav = findViewById(R.id.bottomNavigationView)
    }
}
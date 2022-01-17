package com.annhienktuit.muzikplayer.adapters

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.annhienktuit.muzikplayer.fragments.SingleVideoFragment
import com.annhienktuit.muzikplayer.models.VerticalVideo
import com.google.android.exoplayer2.MediaItem
import java.util.ArrayList

class VideoSliderAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, mediaList: ArrayList<MediaItem>): FragmentStateAdapter(fragmentManager, lifecycle) {
    var mContext: Context? = null
    val TAG = "VideoPlayerFragment"
    val mediaList = mediaList
    override fun createFragment(position: Int): Fragment {
        return SingleVideoFragment(mediaList[position])
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }
}
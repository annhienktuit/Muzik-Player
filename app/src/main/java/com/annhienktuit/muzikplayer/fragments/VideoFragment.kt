package com.annhienktuit.muzikplayer.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.retrofit.RetrofitClient
import android.widget.Toast
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.annhienktuit.muzikplayer.adapters.VideoSliderAdapter
import com.annhienktuit.muzikplayer.models.VerticalVideo
import com.google.android.exoplayer2.MediaItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VideoFragment : Fragment() {
    var listVideo = ArrayList<VerticalVideo>()
    var listMediaItem = ArrayList<MediaItem>()
    private var pagerAdapter: FragmentStateAdapter? = null
    private lateinit var videoViewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initVideoData() {
        val client = RetrofitClient(context!!)
        val call = client.getVideoService().getAllVideos
        call.enqueue(object : Callback<List<VerticalVideo>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<List<VerticalVideo>>,
                response: Response<List<VerticalVideo>>
            ) {
                if(response?.body() == null){
                    Log.i("Nhiennha ","Null")
                    return
                }
                response.body()?.let { listVideo.addAll(it) }
                for(video in listVideo){
                    val mediaItem = MediaItem.fromUri(video.url)
                    listMediaItem.add(mediaItem)
                }
                videoViewPager.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<VerticalVideo>>, t: Throwable) {
                Log.i("Nhiennha ", t.toString())
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        val context = container!!.context
        videoViewPager = view.findViewById(R.id.viewPagerVideo)
        pagerAdapter = VideoSliderAdapter(context,childFragmentManager, lifecycle, listMediaItem)
        videoViewPager.adapter = pagerAdapter
        initVideoData()
        return view
    }

}
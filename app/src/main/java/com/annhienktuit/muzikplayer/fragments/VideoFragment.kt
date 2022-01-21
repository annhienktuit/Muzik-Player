package com.annhienktuit.muzikplayer.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.adapters.VideoSliderAdapter
import com.annhienktuit.muzikplayer.models.Video
import com.annhienktuit.muzikplayer.retrofit.RetrofitClient
import com.google.android.exoplayer2.MediaItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VideoFragment : Fragment() {
    var listVideo = ArrayList<Video>()
    var listMediaItem = ArrayList<MediaItem>()
    private var pagerAdapter: FragmentStateAdapter? = null
    private lateinit var videoViewPager: ViewPager2
    val VIDEO_BASE_URL = "https://61e52105595afe00176e5333.mockapi.io"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)
        val context = container!!.context
        attachView(view)
        pagerAdapter = VideoSliderAdapter(context, childFragmentManager, lifecycle, listMediaItem)
        videoViewPager.adapter = pagerAdapter
        initVideoData()
        return view
    }

    private fun initVideoData() {
        val client = RetrofitClient(requireContext(), VIDEO_BASE_URL)
        val call = client.getVideoService().getAllVideos
        call.enqueue(object : Callback<List<Video>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<List<Video>>,
                response: Response<List<Video>>
            ) {
                if (response.body() == null) {
                    Log.i("Nhiennha ", "Null")
                    return
                }
                response.body()?.let { listVideo.addAll(it) }
                for (video in listVideo) {
                    val mediaItem = MediaItem.fromUri(video.url)
                    listMediaItem.add(mediaItem)
                }
                videoViewPager.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<Video>>, t: Throwable) {
                Log.e("Nhiennha ", t.message.toString())
            }
        })
    }

    private fun attachView(view: View) {
        videoViewPager = view.findViewById(R.id.viewPagerVideo)
    }

}
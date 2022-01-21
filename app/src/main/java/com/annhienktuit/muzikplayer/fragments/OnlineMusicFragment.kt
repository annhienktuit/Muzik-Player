package com.annhienktuit.muzikplayer.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.adapters.TrackListAdapter
import com.annhienktuit.muzikplayer.adapters.TrendingListAdapter
import com.annhienktuit.muzikplayer.models.OnlineTracks
import com.annhienktuit.muzikplayer.models.Track
import com.annhienktuit.muzikplayer.models.TrendingTracks
import com.annhienktuit.muzikplayer.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OnlineMusicFragment : Fragment() {
    private lateinit var recyclerviewTopTrack: RecyclerView
    private lateinit var topTrackAdapter: RecyclerView.Adapter<TrackListAdapter.ViewHolder>
    private lateinit var onlineTracks: OnlineTracks
    private lateinit var chartTracks: TrendingTracks
    private var trackList = ArrayList<Track>()
    private var chartTrackList = ArrayList<Track>()
    private lateinit var recyclerViewTrendingTracks: RecyclerView
    private lateinit var trendingTrackAdapter: RecyclerView.Adapter<TrendingListAdapter.ViewHolder>
    private lateinit var swipeContainer: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_online_music, container, false)
        val context = view.context
        attachView(view)
        topTrackAdapter = TrackListAdapter(context, trackList)
        recyclerviewTopTrack.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerviewTopTrack.adapter = topTrackAdapter
        trendingTrackAdapter = TrendingListAdapter(context, chartTrackList)
        recyclerViewTrendingTracks.layoutManager = GridLayoutManager(context, 2)
        recyclerViewTrendingTracks.adapter = trendingTrackAdapter
        initTrackData()
        initChartData()
        swipeContainer.setOnRefreshListener {
            initChartData()
        }
        return view
    }

    private fun initTrackData() {
        val client = RetrofitClient(requireContext(), "https://api.deezer.com")
        val call = client.getOnlineTrackService().getAlbumTrack("2159765062")
        call.enqueue(object : Callback<OnlineTracks> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<OnlineTracks>,
                response: Response<OnlineTracks>
            ) {
                if (response.body() != null) {
                    onlineTracks = response.body()!!
                    for (track in onlineTracks.tracks) {
                        trackList.add(track)
                    }
                    recyclerviewTopTrack.adapter?.notifyDataSetChanged()
                } else {
                    Log.i("Nhiennha ", "Null")
                }
            }

            override fun onFailure(call: Call<OnlineTracks>, t: Throwable) {
                Log.e("Nhiennha ", t.fillInStackTrace().toString())
            }

        })
    }

    private fun initChartData() {
        val client = RetrofitClient(requireContext(), "https://api.deezer.com")
        val call = client.getChartService().getChartTracks()
        call.enqueue(object : Callback<TrendingTracks> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<TrendingTracks>,
                response: Response<TrendingTracks>
            ) {
                if (response.body() != null) {
                    //refresh handle
                    chartTrackList.clear()
                    recyclerViewTrendingTracks.adapter?.notifyDataSetChanged()
                    //re-update
                    chartTracks = response.body()!!
                    for (track in chartTracks.tracks.tracks) {
                        chartTrackList.add(track)
                    }
                    recyclerViewTrendingTracks.adapter?.notifyDataSetChanged()
                } else {
                    Log.i("Nhiennha ", "Null")
                }
            }

            override fun onFailure(call: Call<TrendingTracks>, t: Throwable) {
                Log.e("Nhiennha ", t.fillInStackTrace().toString())
            }

        })
        swipeContainer.isRefreshing = false
    }

    private fun attachView(view: View) {
        recyclerviewTopTrack = view.findViewById(R.id.recyclerViewTopTracks)
        recyclerViewTrendingTracks = view.findViewById(R.id.recyclerviewTrending)
        swipeContainer = view.findViewById(R.id.swipeContainer)
    }

}
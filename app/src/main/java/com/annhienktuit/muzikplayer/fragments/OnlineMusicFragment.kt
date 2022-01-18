package com.annhienktuit.muzikplayer.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.models.OnlineTracks
import com.annhienktuit.muzikplayer.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OnlineMusicFragment : Fragment() {
    private lateinit var recyclerviewTopTrack: RecyclerView
    //private lateinit var adapter: RecyclerView.Adapter<AlbumItemAdapter.ViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun initTrackData() {
        val client = RetrofitClient(requireContext(),"https://api.deezer.com")
        val call = client.getOnlineTrackService().getAlbumTrack("2159765062")
        call.enqueue(object : Callback<OnlineTracks> {
            override fun onResponse(
                call: Call<OnlineTracks>,
                response: Response<OnlineTracks>
            ) {
                if(response.body() != null){
                    Log.i("Nhiennha ", response.body()!!.toString())
                }
                else {
                    Log.i("Nhiennha ","Null")
                }
            }

            override fun onFailure(call: Call<OnlineTracks>, t: Throwable) {
                Log.e("Nhiennha ",t.fillInStackTrace().toString())
            }

        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_online_music, container, false)
        initTrackData()
        return view
    }

}
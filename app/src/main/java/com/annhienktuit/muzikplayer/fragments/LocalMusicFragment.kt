package com.annhienktuit.muzikplayer.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.adapters.LocalListAdapter
import com.annhienktuit.muzikplayer.models.LocalTrack
import java.io.File


class LocalMusicFragment : Fragment() {

    private var listLocalSong = ArrayList<File>()
    private val sampleThumbnailArt = "https://static-zmp3.zadn.vn/skins/common/logo600.png"
    private lateinit var recyclerViewLocalTrack: RecyclerView
    private lateinit var localTrackAdapter: RecyclerView.Adapter<LocalListAdapter.ViewHolder>
    private lateinit var localTrackList: ArrayList<LocalTrack>
    private lateinit var localSwipeContainer: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_local_music, container, false)
        val context = view.context
        recyclerViewLocalTrack = view.findViewById(R.id.recyclerViewLocalTracks)
        localSwipeContainer = view.findViewById(R.id.localSwipeContainer)
        localTrackList = getAllAudioFromDevice(context)
        localTrackAdapter = LocalListAdapter(context, localTrackList)
        recyclerViewLocalTrack.layoutManager = LinearLayoutManager(context)
        recyclerViewLocalTrack.adapter = localTrackAdapter
        localSwipeContainer.setOnRefreshListener {
            localTrackList = getAllAudioFromDevice(context)
            recyclerViewLocalTrack.adapter?.notifyDataSetChanged()
            localSwipeContainer.isRefreshing = false
        }
        return view
    }

    private fun getAllAudioFromDevice(context: Context): ArrayList<LocalTrack> {
        val listLocalTrack: ArrayList<LocalTrack> = ArrayList()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.ArtistColumns.ARTIST,
        )

        val query: Cursor? = context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )

        if (query != null) {
            while (query.moveToNext()) {
                val path = query.getString(0)
                val localTrack = LocalTrack(
                    path.substring(path.lastIndexOf("/") + 1), //title
                    query.getString(2), //artist
                    path, //path
                    query.getString(1), //album
                    sampleThumbnailArt
                )//thumbnail
                if(localTrack.title.substring(localTrack.title.length - 3, localTrack.title.length) != "ogg"){
                listLocalTrack.add(localTrack)
                }
            }
            query.close()
        }
        return listLocalTrack
    }

}
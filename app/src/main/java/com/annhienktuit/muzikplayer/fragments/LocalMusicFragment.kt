package com.annhienktuit.muzikplayer.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.annhienktuit.muzikplayer.R
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.annhienktuit.muzikplayer.activities.PlayerActivity
import com.annhienktuit.muzikplayer.adapters.LocalListAdapter
import com.annhienktuit.muzikplayer.adapters.TrackListAdapter
import com.annhienktuit.muzikplayer.models.LocalTrack
import java.io.File

class LocalMusicFragment : Fragment() {

    private var listLocalSong = ArrayList<File>()
    private val sampleThumbnailArt = "https://static-zmp3.zadn.vn/skins/common/logo600.png"
    private lateinit var recyclerViewLocalTrack:RecyclerView
    private lateinit var localTrackAdapter: RecyclerView.Adapter<LocalListAdapter.ViewHolder>
    private lateinit var localTrackList: ArrayList<LocalTrack>
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
        localTrackList = getAllAudioFromDevice(context)
        localTrackAdapter = LocalListAdapter(context, localTrackList)
        recyclerViewLocalTrack.layoutManager = LinearLayoutManager(context)
        recyclerViewLocalTrack.adapter = localTrackAdapter
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
        val query: Cursor? = context.contentResolver.query(uri,
            projection,
            null,
            null,
            null)

        if (query != null) {
            while (query.moveToNext()) {
                val path = query.getString(0)
                val audioModel = LocalTrack(
                    path.substring(path.lastIndexOf("/") + 1), //title
                    query.getString(2), //artist
                    path, //path
                    query.getString(1), //thumbnail
                    sampleThumbnailArt)
                listLocalTrack.add(audioModel)
            }
            query.close()
        }
        return listLocalTrack
    }


}
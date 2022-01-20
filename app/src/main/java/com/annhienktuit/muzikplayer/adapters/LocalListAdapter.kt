package com.annhienktuit.muzikplayer.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.activities.PlayerActivity
import com.annhienktuit.muzikplayer.models.LocalTrack
import com.bumptech.glide.Glide

class LocalListAdapter(context: Context, localTracksList: List<LocalTrack>) :
    RecyclerView.Adapter<LocalListAdapter.ViewHolder>() {
    private var mContext: Context = context
    private var localList: List<LocalTrack> = localTracksList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.local_track_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var track = localList[position]
        holder.itemView.tag = localList[position]
        holder.localTrackTitle.text = track.title
        holder.localTrackArtist.text = track.artist
        Glide.with(mContext).load(R.drawable.ic_logo).placeholder(R.drawable.ic_logo).into(holder.albumArt)
    }

    override fun getItemCount(): Int {
        return localList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var localTrackTitle: TextView = itemView.findViewById(R.id.tvLocalTrackTitle)
        var localTrackArtist: TextView = itemView.findViewById(R.id.tvLocalTrackArtist)
        var albumArt: ImageView = itemView.findViewById(R.id.imgLocalTrackArtwork)
        init {
            val listPath = ArrayList<String>()
            val listArtwork = ArrayList<String>()
            val listTitle = ArrayList<String>()
            val listArtist = ArrayList<String>()
            for (item in localList) {
                listPath.add(item.path)
                listArtwork.add(item.artworkURL)
                listTitle.add(item.title)
                listArtist.add(item.artist)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, PlayerActivity::class.java)
                    val currentIndex = bindingAdapterPosition
                    //TODO: Find solution for this
                    intent.putExtra("listURL", listPath)
                    intent.putExtra("listArtwork", listArtwork)
                    intent.putExtra("listTitle", listTitle)
                    intent.putExtra("listArtist", listArtist)
                    intent.putExtra("Index", currentIndex)
                    intent.putExtra("isLocal",true)
                    itemView.context.startActivity(intent)

                }
            }
        }
    }
}
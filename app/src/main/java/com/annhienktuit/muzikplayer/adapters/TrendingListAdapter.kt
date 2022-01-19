package com.annhienktuit.muzikplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.models.Track
import com.bumptech.glide.Glide

class TrendingListAdapter(context: Context, trendingTracksList: List<Track>) :
    RecyclerView.Adapter<TrendingListAdapter.ViewHolder>() {
    private var mContext: Context = context
    private var trendingList: List<Track> = trendingTracksList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.trending_track_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var album = trendingList[position]
        holder.itemView.tag = trendingList[position]
        holder.albumTitle.text = album.title
        Glide.with(mContext).load(trendingList[position].album.artworkURL).placeholder(R.drawable.sample_daily).into(holder.albumArt)
    }

    override fun getItemCount(): Int {
        return trendingList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var albumTitle: TextView
        var albumArt: ImageView
        init {
            albumTitle = itemView.findViewById(R.id.tvTrendingTrackTitle)
            albumArt = itemView.findViewById(R.id.imgTrendingTrack)
        }

    }
}
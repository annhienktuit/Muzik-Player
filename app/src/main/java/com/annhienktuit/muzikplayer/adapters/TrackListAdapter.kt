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
import com.bumptech.glide.load.engine.DiskCacheStrategy

class TrackListAdapter(context: Context, trackList: ArrayList<Track>) :
    RecyclerView.Adapter<TrackListAdapter.ViewHolder>() {
    private var mContext: Context = context
    private var trackList: List<Track> = trackList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentTrack = trackList[position]
        holder.itemView.tag = trackList[position]
        holder.trackTitle.text = currentTrack.title
        holder.trackArtist.text = currentTrack.artist.artistName
        Glide.with(mContext)
            .load(currentTrack.album.artworkURL)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.sample_daily)
            .into(holder.trackArtwork)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var trackTitle: TextView = itemView.findViewById(R.id.tvTrackTitle)
        var trackArtist: TextView = itemView.findViewById(R.id.tvTrackArtist)
        var trackArtwork: ImageView = itemView.findViewById(R.id.imgTrackArtwork)

    }

}

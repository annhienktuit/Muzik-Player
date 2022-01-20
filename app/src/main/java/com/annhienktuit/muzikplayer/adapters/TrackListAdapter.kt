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

        init {
            var listID = ArrayList<String>()
            var listURL = ArrayList<String>()
            var listArtwork = ArrayList<String>()
            var listTitle = ArrayList<String>()
            var listArtist = ArrayList<String>()
            for (item in trackList) {
                listID.add(item.id.toString())
                listURL.add(item.trackURL)
                listArtwork.add(item.album.artworkURL)
                listTitle.add(item.title)
                listArtist.add(item.artist.artistName)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, PlayerActivity::class.java)
                    val currentIndex = bindingAdapterPosition
                    //TODO: Find solution for this
                    intent.putExtra("listID", listID)
                    intent.putExtra("listURL", listURL)
                    intent.putExtra("listArtwork", listArtwork)
                    intent.putExtra("listTitle", listTitle)
                    intent.putExtra("listArtist", listArtist)
                    intent.putExtra("Index", currentIndex)
                    itemView.context.startActivity(intent)

                }
            }
        }
    }
}



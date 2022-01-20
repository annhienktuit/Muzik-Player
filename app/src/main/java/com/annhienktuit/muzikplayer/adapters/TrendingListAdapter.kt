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
        var albumTitle: TextView = itemView.findViewById(R.id.tvTrendingTrackTitle)
        var albumArt: ImageView = itemView.findViewById(R.id.imgTrendingTrack)
        init {
            var listID = ArrayList<String>()
            var listURL = ArrayList<String>()
            var listArtwork = ArrayList<String>()
            var listTitle = ArrayList<String>()
            var listArtist = ArrayList<String>()
            for (item in trendingList) {
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
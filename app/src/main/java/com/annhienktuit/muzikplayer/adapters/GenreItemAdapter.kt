package com.annhienktuit.muzikplayer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.models.Genre
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlin.concurrent.thread

class GenreItemAdapter(context: Context, genreList: List<Genre>) :
    RecyclerView.Adapter<GenreItemAdapter.ViewHolder>() {
    private var mContext: Context = context
    var genreList: List<Genre> = genreList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.genre_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var genre = genreList[position]
        holder.itemView.tag = genreList[position]
        holder.genreTitle.text = genre.name
        Glide.with(mContext)
            .load(genreList[position].artworkURL)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.sample_daily)
            .into(holder.genreArtwork)
        var idx = 0
        for(i in 1..6) {
            if(position % i == 0) idx = i
        }
        setCardViewBackgroundColor(idx, holder)
    }

    @SuppressLint("ResourceAsColor")
    private fun setCardViewBackgroundColor(random: Int, holder:ViewHolder) {
        when (random) {
            1 -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.card_bg_1))
            2 -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.card_bg_2))
            3 -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.card_bg_3))
            4 -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.card_bg_4))
            5 -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.card_bg_5))
            6 -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.card_bg_6))
        }
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var genreTitle: TextView = itemView.findViewById(R.id.tvGenreTitle)
        var genreArtwork: ImageView = itemView.findViewById(R.id.imgGenre)
        var cardView: CardView = itemView.findViewById(R.id.cardviewGenre)

        init {
            itemView.setOnClickListener {
                Toast.makeText(mContext, "Will be released in the next version", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
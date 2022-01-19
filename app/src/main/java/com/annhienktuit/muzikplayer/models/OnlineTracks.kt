package com.annhienktuit.muzikplayer.models

import com.google.gson.annotations.SerializedName

data class OnlineTracks(
    @SerializedName("data")
    val tracks: ArrayList<Track>
)


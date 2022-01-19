package com.annhienktuit.muzikplayer.models

import com.google.gson.annotations.SerializedName

data class TrendingTracks (
    @SerializedName("tracks")
    val tracks: OnlineTracks
)
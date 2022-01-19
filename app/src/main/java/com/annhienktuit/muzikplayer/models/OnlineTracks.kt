package com.annhienktuit.muzikplayer.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OnlineTracks(
    @SerializedName("data")
    val tracks: ArrayList<Track>
){

}


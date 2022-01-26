package com.annhienktuit.muzikplayer.models

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture_medium")
    val artworkURL: String
)

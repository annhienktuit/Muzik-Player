package com.annhienktuit.muzikplayer.models

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("id")
    private val id: Int = 0,

    @SerializedName("title")
    private val title: String? = null,

    @SerializedName("preview")
    private val trackURL: String? = null,

    @SerializedName("artist")
    private val artist: Artist,

    @SerializedName("album")
    var album: Album
)

data class Artist(
    @SerializedName("name")
    var artistName: String
)

data class Album(
    @SerializedName("cover_big")
    var artworkURL: String
)
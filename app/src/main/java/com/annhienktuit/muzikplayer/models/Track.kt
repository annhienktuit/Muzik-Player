package com.annhienktuit.muzikplayer.models

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("title")
    var title: String,

    @SerializedName("preview")
    var trackURL: String,

    @SerializedName("artist")
    var artist: Artist,

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

//Sample API: https://api.deezer.com/playlist/2159765062/tracks
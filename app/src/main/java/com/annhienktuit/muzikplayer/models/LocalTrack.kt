package com.annhienktuit.muzikplayer.models

data class LocalTrack(
    val title:String,
    val artist:String,
    var path:String,
    val album:String,
    val artworkURL: String)
package com.annhienktuit.muzikplayer.models

import com.google.gson.annotations.SerializedName

class Video {
    @SerializedName("id")
    var id: String = ""
    @SerializedName("url")
    var url: String = ""
    @SerializedName("key")
    var key: String = ""
}
package com.annhienktuit.muzikplayer.retrofit

import com.annhienktuit.muzikplayer.models.VerticalVideo
import retrofit2.Call
import retrofit2.http.GET

interface GetVideoService {
    @get:GET("/api/v1/videos")
    val getAllVideos: Call<List<VerticalVideo>>
}
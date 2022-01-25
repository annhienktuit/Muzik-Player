package com.annhienktuit.muzikplayer.retrofit

import com.annhienktuit.muzikplayer.models.OnlineTracks
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetTrackService {
    @GET("/playlist/{albumID}/tracks")
    fun getAlbumTrack(@Path("albumID") id:String): Observable<OnlineTracks>
}
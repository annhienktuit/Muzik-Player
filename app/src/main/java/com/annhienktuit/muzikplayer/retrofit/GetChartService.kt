package com.annhienktuit.muzikplayer.retrofit

import com.annhienktuit.muzikplayer.models.OnlineTracks
import com.annhienktuit.muzikplayer.models.TrendingTracks
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetChartService {
    @GET("/chart/0?limit=9")
    fun getChartTracks(): Observable<TrendingTracks>
}
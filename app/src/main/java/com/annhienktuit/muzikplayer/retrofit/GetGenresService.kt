package com.annhienktuit.muzikplayer.retrofit

import com.annhienktuit.muzikplayer.models.Genres
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface GetGenresService {
    @GET("/genre")
    fun getGenresList(): Observable<Genres>
}
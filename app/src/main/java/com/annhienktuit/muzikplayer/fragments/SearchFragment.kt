package com.annhienktuit.muzikplayer.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.adapters.GenreItemAdapter
import com.annhienktuit.muzikplayer.models.Genre
import com.annhienktuit.muzikplayer.models.Genres
import com.annhienktuit.muzikplayer.retrofit.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchFragment : Fragment() {
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var recyclerviewGenre: RecyclerView
    var genreList = ArrayList<Genre>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val context = view.context
        compositeDisposable = CompositeDisposable()
        recyclerviewGenre = view.findViewById(R.id.recyclerViewGenres)
        recyclerviewGenre.setHasFixedSize(true)
        recyclerviewGenre.layoutManager = GridLayoutManager(context, 2)
        recyclerviewGenre.adapter = GenreItemAdapter(context, genreList)
        initGenresData()
        return view
    }

    private fun initGenresData() {
        val client = RetrofitClient(requireContext(), "https://api.deezer.com")
        val disposable = client.getGenresListService().getGenresList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleGenresResponse, this::handleError, this::handleSuccess)
        compositeDisposable.add(disposable)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleGenresResponse(genresList: Genres) {
        for(genre in genresList.genres){
            if(genre.name != "All") genreList.add(genre)
        }
        recyclerviewGenre?.adapter?.notifyDataSetChanged()
    }

    private fun handleSuccess() {
        Log.i("Nhiennha ", "Get genres data success")
    }

    private fun handleError(throwable: Throwable) {
        Log.e("Nhiennha ", throwable.message!!)
    }

}
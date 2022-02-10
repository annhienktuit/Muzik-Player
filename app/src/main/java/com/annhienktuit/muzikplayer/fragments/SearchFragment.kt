package com.annhienktuit.muzikplayer.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.adapters.GenreItemAdapter
import com.annhienktuit.muzikplayer.models.Genre
import com.annhienktuit.muzikplayer.models.Genres
import com.annhienktuit.muzikplayer.retrofit.RetrofitClient
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class SearchFragment : Fragment() {
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var recyclerviewGenre: RecyclerView
    private lateinit var trendingLineChart: LineChart
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
        attachView(view)
        showChartData(context)
        recyclerviewGenre.setHasFixedSize(true)
        recyclerviewGenre.layoutManager = GridLayoutManager(context, 2)
        recyclerviewGenre.adapter = GenreItemAdapter(context, genreList)
        initGenresData()
        return view
    }

    private fun showChartData(context: Context) {
        val top1List = arrayListOf(1, 3, 3, 4, 3, 5)
        val top2List = arrayListOf(2, 2, 4, 5, 4, 4)
        val top1DataEntry = ArrayList<BarEntry>()
        val top2DataEntry = ArrayList<BarEntry>()
        top1List.reverse()
        top2List.reverse()
        for(i in 0 until 6){
            val idx = i.toFloat()
            top1DataEntry.add(BarEntry(idx,top1List[i].toFloat()))
            top2DataEntry.add(BarEntry(idx,top2List[i].toFloat()))
        }
        val chartData = LineData()
        val set1 = LineDataSet(top1DataEntry as List<Entry>?, "Top 1")
        setLineColor(context, set1, R.color.spot_green)
        val set2 = LineDataSet(top2DataEntry as List<Entry>?, "Top2")
        setLineColor(context, set2, R.color.white)
        chartData.addDataSet(set1)
        chartData.addDataSet(set2)
        customChartBorder(trendingLineChart)
        trendingLineChart.data = chartData
        trendingLineChart.invalidate()
    }

    private fun customChartBorder(trendingLineChart: LineChart) {
        trendingLineChart.axisLeft.isInverted = true
        trendingLineChart.legend.isEnabled = false
        trendingLineChart.axisLeft.setDrawGridLines(false)
        trendingLineChart.axisLeft.setDrawAxisLine(false)
        trendingLineChart.axisRight.setDrawGridLines(false)
        trendingLineChart.axisRight.setDrawAxisLine(false)
        trendingLineChart.xAxis.setDrawGridLines(false)
        trendingLineChart.xAxis.setDrawAxisLine(false)
        trendingLineChart.setDrawBorders(false)
    }

    private fun setLineColor(context: Context, line: LineDataSet, color: Int) {
        val color = ContextCompat.getColor(context, color)
        line.color = color
        line.setDrawHorizontalHighlightIndicator(false)
        line.setDrawVerticalHighlightIndicator(false)

    }

    private fun attachView(view: View) {
        recyclerviewGenre = view.findViewById(R.id.recyclerViewGenres)
        trendingLineChart = view.findViewById(R.id.trendingLineChart)
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
        recyclerviewGenre.adapter?.notifyDataSetChanged()
    }

    private fun handleSuccess() {
        Log.i("Nhiennha ", "Get genres data success")
    }

    private fun handleError(throwable: Throwable) {
        Log.e("Nhiennha ", throwable.message!!)
    }

}
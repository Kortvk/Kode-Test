package com.example.kortikov.kodetest.SearchCity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import com.example.kortikov.kodetest.ApiService.CityApi
import com.example.kortikov.kodetest.City
import com.example.kortikov.kodetest.CityList
import com.google.android.gms.location.LocationRequest
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.patloew.rxlocation.RxLocation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.math.sqrt

class SearchInteractor(var context: Context){
    private val rxLocation = RxLocation(context)

    private val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5000)
            .setNumUpdates(1)

    init {
        rxLocation.setDefaultTimeout(10, TimeUnit.SECONDS)
    }

    private fun searchItem(search : String, list: List<City>) : List<City>{
        val result = ArrayList<City>()
        for (city in list)
            if (city.city!!.toUpperCase().contains(search.toUpperCase()))
                result.add(city)

        return result
    }

    private var listCity: List<City>? = null

    fun load(flag : Boolean) : Observable<SearchViewState>{
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.meetup.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val service = retrofit.create(CityApi::class.java)
        return service.getCities("ru").map { list: CityList -> listCity = list.getResults()
            SearchViewState.LoadResult(list.getResults()) as SearchViewState}
                .startWith(SearchViewState.Loading())
                .onErrorReturn { t: Throwable -> SearchViewState.Error(t) }
                .subscribeOn(Schedulers.io())
    }

    fun search(searchText : String) : Observable<SearchViewState>{
        if (searchText.isEmpty())
            return Observable.just(SearchViewState.SearchResult(listCity))

        return Observable.just(SearchViewState.SearchResult(searchItem(searchText, listCity!!)) as SearchViewState)
                .onErrorReturn {
                    SearchViewState.Error(it)
                }
    }

    fun searchCity(location: Location?): City? {
        if (listCity == null) return null
        var city = City()
        var min = Double.MAX_VALUE
        for(ct in listCity!!){
            var len = sqrt(Math.pow(location!!.latitude - ct.lat!!, 2.0) + Math.pow(location.longitude - ct.lon!!, 2.0))
            if (len < min){
                min = len
                city = ct
            }
        }
        return city
    }

    @SuppressLint("MissingPermission")
    fun location(flag: Boolean): Observable<SearchViewState>{
        return rxLocation.location().updates(locationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    SearchViewState.Location(searchCity(it)) as SearchViewState
                }
                .startWith(SearchViewState.LocationRefresh())
    }
}
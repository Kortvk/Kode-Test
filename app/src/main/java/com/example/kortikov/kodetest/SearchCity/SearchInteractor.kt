package com.example.kortikov.kodetest.SearchCity

import android.annotation.SuppressLint
import android.location.LocationManager
import com.example.kortikov.kodetest.ApiService.CityApi
import com.example.kortikov.kodetest.City
import com.example.kortikov.kodetest.CityList
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchInteractor {

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

    @SuppressLint("MissingPermission")
    fun location(locationManager: LocationManager?): Observable<SearchViewState>{
        if (locationManager != null){
            var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) return Observable.just(SearchViewState.Location(location))
            else{
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                if (location != null) return Observable.just(SearchViewState.Location(location))
                else{
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (location != null) return Observable.just(SearchViewState.Location(location))
                }
            }
        }
        return Observable.just(SearchViewState.Location() as SearchViewState)
    }
}
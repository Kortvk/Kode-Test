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
            SearchViewState(result = list.getResults())
        }
                .startWith(SearchViewState(loading = true))
                .onErrorReturn { t: Throwable -> SearchViewState(error = t.message) }
                .subscribeOn(Schedulers.io())
    }

    fun search(searchText : String) : Observable<SearchViewState>{
        if (searchText.isEmpty())
            return Observable.just(SearchViewState(result = listCity))

        return Observable.just(SearchViewState(searchText = searchText, result = searchItem(searchText, listCity!!)))
                .onErrorReturn {
                    SearchViewState(error = it.message, searchText = searchText)
                }
    }

    @SuppressLint("MissingPermission")
    fun location(locationManager: LocationManager?): Observable<SearchViewState>{
        if (locationManager != null){
            var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) return Observable.just(SearchViewState(currentLocation = location))
            else{
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                if (location != null) return Observable.just(SearchViewState(currentLocation = location))
                else{
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (location != null) return Observable.just(SearchViewState(currentLocation = location))
                }
            }
        }
        return Observable.just(SearchViewState())
    }
}
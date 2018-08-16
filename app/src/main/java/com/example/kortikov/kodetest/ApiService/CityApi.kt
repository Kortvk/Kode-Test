package com.example.kortikov.kodetest.ApiService

import com.example.kortikov.kodetest.CityList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApi {
    @GET("2/cities.php")
    fun getCities(@Query("country") country : String): Observable<CityList>
}
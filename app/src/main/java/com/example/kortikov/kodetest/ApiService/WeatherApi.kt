package com.example.kortikov.kodetest.ApiService

import com.example.kortikov.kodetest.WeatherList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/forecast?units=metric&lang=ru")
    fun getWeatherByCoordinate(@Query("APPID") appid : String, @Query("lon") lon : Double, @Query("lat") lat : Double) : Observable<WeatherList>
}
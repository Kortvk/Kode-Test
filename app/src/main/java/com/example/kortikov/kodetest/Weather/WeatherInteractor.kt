package com.example.kortikov.kodetest.Weather

import com.example.kortikov.kodetest.ApiService.WeatherApi
import com.example.kortikov.kodetest.Booking.BookingActivity
import com.example.kortikov.kodetest.City
import com.example.kortikov.kodetest.WeatherList
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherInteractor {

    fun weatherFrom(city: City?): Observable<WeatherViewState>{
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val service = retrofit.create(WeatherApi::class.java)
        return service.getWeatherByCoordinate(BookingActivity.APP_ID, city!!.lon!!, city.lat!!)
                .map { t: WeatherList -> WeatherViewState.WeatherFrom(t) as WeatherViewState }
                .onErrorReturn { t: Throwable -> WeatherViewState.Error(t) }
                .subscribeOn(Schedulers.io())
    }

    fun weatherTo(city: City?): Observable<WeatherViewState>{
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val service = retrofit.create(WeatherApi::class.java)
        return service.getWeatherByCoordinate(BookingActivity.APP_ID, city!!.lon!!, city.lat!!)
                .map { t: WeatherList -> WeatherViewState.WeatherTo(t) as WeatherViewState }
                .onErrorReturn { t: Throwable -> WeatherViewState.Error(t) }
                .subscribeOn(Schedulers.io())
    }
}

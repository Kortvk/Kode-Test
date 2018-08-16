package com.example.kortikov.kodetest.Weather

import com.example.kortikov.kodetest.City
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface WeatherView : MvpView {
    var weatherFromIntent: Observable<City>
    var weatherToIntent: Observable<City>
    fun render(viewState: WeatherViewState)
}
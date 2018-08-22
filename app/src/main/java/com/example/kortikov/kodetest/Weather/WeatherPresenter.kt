package com.example.kortikov.kodetest.Weather

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class WeatherPresenter : MviBasePresenter<WeatherView, WeatherViewState>() {
    private val weatherIntersector = WeatherInteractor()

    override fun bindIntents() {
        val weatherFrom = intent(WeatherView::weatherFromIntent)
                .flatMap(weatherIntersector::weatherFrom)
                .observeOn(AndroidSchedulers.mainThread())
        val weatherTo = intent(WeatherView::weatherToIntent)
                .flatMap(weatherIntersector::weatherTo)
                .observeOn(AndroidSchedulers.mainThread())

        val allIntents = Observable.merge(weatherFrom, weatherTo)

        subscribeViewState(allIntents, WeatherView::render)
    }
}
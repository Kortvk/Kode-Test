package com.example.kortikov.kodetest.Weather

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class WeatherPresenter : MviBasePresenter<WeatherView, WeatherViewState>() {
    private val weatherIntersector = WeatherIntersector()

    override fun bindIntents() {
        val weatherFrom = intent(WeatherView::weatherFromIntent)
                .flatMap(weatherIntersector::weatherFrom)
                .observeOn(AndroidSchedulers.mainThread())
        val weatherTo = intent(WeatherView::weatherToIntent)
                .flatMap(weatherIntersector::weatherTo)
                .observeOn(AndroidSchedulers.mainThread())

        val allIntents = Observable.merge(weatherFrom, weatherTo)

       // var initialState = WeatherViewState()
       // var stateObservable = allIntents.scan(initialState, this::viewStateReducer)
        subscribeViewState(allIntents, WeatherView::render)
    }

//    private fun viewStateReducer(previousState: WeatherViewState, changes: WeatherPartialState): WeatherViewState {
//        if (changes.weatherFrom != null && previousState.weatherFrom == null){
//            previousState.weatherFrom = changes.weatherFrom
//            return previousState
//        }
//        if (changes.weatherTo != null && previousState.weatherTo == null){
//            previousState.weatherTo = changes.weatherTo
//            return previousState
//        }
//        return previousState
//    }
}
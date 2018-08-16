package com.example.kortikov.kodetest.Buy

import com.example.kortikov.kodetest.City
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import java.util.*

interface BuyView : MvpView {
    var setDepartureCityIntent : Observable<City>
    var setArriveCityIntent : Observable<City>
    var setDepartureDateIntent : Observable<Calendar>
    var setReturnDateIntent : Observable<Calendar>
    var addAdultIntent : Observable<Int>
    var removeAdultIntent : Observable<Int>
    var addKidIntent : Observable<Int>
    var removeKidIntent : Observable<Int>
    var addBabyIntent : Observable<Int>
    var removeBabyIntent : Observable<Int>
    var reverseCitiesIntent: Observable<Boolean>
    var removeReturnDateIntent: Observable<Boolean>

    /**
     *Отрисовка View
     * @param viewState The current viewState state that should be displayed
     */
    fun render(viewState: BuyViewState)
}
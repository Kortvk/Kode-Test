package com.example.kortikov.kodetest.Booking

import com.example.kortikov.kodetest.City
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import java.util.*

interface BookingView : MvpView {
    var setDepartureCityIntent : Observable<City>
    var setArriveCityIntent : Observable<City>
    var setDepartureDateIntent : Observable<Calendar>
    var setReturnDateIntent : Observable<Calendar>
    var changeAdultIntent : Observable<Int>
    var changeKidIntent : Observable<Int>
    var changeBabyIntent : Observable<Int>
    var reverseCitiesIntent: Observable<Boolean>
    var removeReturnDateIntent: Observable<Boolean>

    fun render(viewState: BookingViewState)
}
package com.example.kortikov.kodetest.SearchCity

import android.location.LocationManager
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface SearchView : MvpView{
    var searchIntent : Observable<String>
    var loadIntent : Observable<Boolean>
    var locationIntent: Observable<Boolean>
    fun render(viewState: SearchViewState)
}
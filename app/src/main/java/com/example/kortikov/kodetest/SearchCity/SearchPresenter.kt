package com.example.kortikov.kodetest.SearchCity

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class SearchPresenter : MviBasePresenter<SearchView, SearchViewState>() {
    private val searchInteractor = SearchInteractor()

    override fun bindIntents() {
        var search = intent(SearchView::searchIntent)
                .flatMap(searchInteractor::search)
                .observeOn(AndroidSchedulers.mainThread())

        var load = intent(SearchView::loadIntent)
                .flatMap(searchInteractor::load)
                .observeOn(AndroidSchedulers.mainThread())

        var location = intent(SearchView::locationIntent)
                .flatMap(searchInteractor::location)
                .observeOn(AndroidSchedulers.mainThread())

        var allIntents = Observable.merge(search, load, location )
        subscribeViewState(allIntents, SearchView::render)
    }

}
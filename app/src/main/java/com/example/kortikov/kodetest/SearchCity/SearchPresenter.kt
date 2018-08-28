package com.example.kortikov.kodetest.SearchCity

import android.content.Context
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class SearchPresenter(val context: Context) : MviBasePresenter<SearchView, SearchViewState>() {
    private val searchInteractor = SearchInteractor(context)

    override fun bindIntents() {
        val search = intent(SearchView::searchIntent)
                .switchMap(searchInteractor::search)
                .observeOn(AndroidSchedulers.mainThread())

        val load = intent(SearchView::loadIntent)
                .flatMap(searchInteractor::load)
                .observeOn(AndroidSchedulers.mainThread())

        val location = intent(SearchView::locationIntent)
                .flatMap(searchInteractor::location)
                .observeOn(AndroidSchedulers.mainThread())

        val allIntents = Observable.merge(search, load, location )
        subscribeViewState(allIntents, SearchView::render)
    }

}
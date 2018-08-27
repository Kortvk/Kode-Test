package com.example.kortikov.kodetest.SearchCity

import android.location.Location
import com.example.kortikov.kodetest.City

interface SearchViewState {
    class Loading : SearchViewState
    data class LoadResult(var result: List<City>? = null) : SearchViewState
    data class SearchResult(var result: List<City>? = null) : SearchViewState
    data class Error(var error: Throwable? = null): SearchViewState
    data class Location(var location: City? = null): SearchViewState
    class LocationRefresh: SearchViewState
}

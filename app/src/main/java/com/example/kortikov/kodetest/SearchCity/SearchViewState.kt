package com.example.kortikov.kodetest.SearchCity

import android.location.Location
import com.example.kortikov.kodetest.City

class SearchViewState (
        var error: String? = null,// if not null, an error has occurred
        var loading: Boolean = false,// if true loading data is in progress
        var searchText: String? = null,
        var result: List<City>? = null,//if not null this is the result of the search
        var currentLocation: Location? = null)//if not null this is the user's location

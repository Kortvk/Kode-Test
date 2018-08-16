package com.example.kortikov.kodetest.Weather

import com.example.kortikov.kodetest.WeatherList

class WeatherViewState(var weatherFrom: WeatherList? = null,
                       var weatherTo: WeatherList? = null,
                       var errorMessage: String? = null)
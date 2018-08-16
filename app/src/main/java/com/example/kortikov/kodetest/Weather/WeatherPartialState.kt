package com.example.kortikov.kodetest.Weather

import com.example.kortikov.kodetest.WeatherList

class WeatherPartialState(var weatherFrom: WeatherList? = null,
                          var weatherTo: WeatherList? = null,
                          var errorMessage: String? = null)
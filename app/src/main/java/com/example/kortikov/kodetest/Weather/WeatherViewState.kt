package com.example.kortikov.kodetest.Weather

import com.example.kortikov.kodetest.WeatherList

interface WeatherViewState{
    data class WeatherFrom(var result: WeatherList? = null) : WeatherViewState
    data class WeatherTo(var result: WeatherList? = null) : WeatherViewState
    data class Error(var error: Throwable? = null) : WeatherViewState
}
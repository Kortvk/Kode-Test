package com.example.kortikov.kodetest.Buy

import com.example.kortikov.kodetest.City
import java.util.*

class BuyViewState(var adultCounter: Int = 0,
                   var kidCounter: Int = 0,
                   var babyCounter: Int = 0,
                   var departureCity: City? = null,
                   var arriveCity: City? = null,
                   var dateDeparture: Calendar? = null,
                   var dateReturn: Calendar? = null,
                   var error:Throwable? = null,
                   var counter: Int = 0,
                   var errorText:String? = null)
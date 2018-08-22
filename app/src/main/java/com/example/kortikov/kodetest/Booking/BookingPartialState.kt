package com.example.kortikov.kodetest.Booking

import com.example.kortikov.kodetest.City
import java.util.*

interface BookingPartialState{

    data class ChangeAdultCounter(var counter: Int = 0) : BookingPartialState
    data class ChangeKidCounter(var counter: Int = 0) : BookingPartialState
    data class ChangeBabyCounter(var counter: Int = 0) : BookingPartialState
    data class SetDepartureCity(var city: City? = null) : BookingPartialState
    data class SetArriveCity(var city: City? = null) : BookingPartialState
    class ReverseCities : BookingPartialState
    data class SetDepartureDate(var date: Calendar? = null) : BookingPartialState
    data class SetReturnDate(var date: Calendar? = null) : BookingPartialState
    data class Error(var error: Throwable? = null) : BookingPartialState
    class NotChanging : BookingPartialState
    //TODO проверить SetReturnDate на removeReturnDate
}
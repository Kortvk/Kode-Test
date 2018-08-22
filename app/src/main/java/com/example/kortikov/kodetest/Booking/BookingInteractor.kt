package com.example.kortikov.kodetest.Booking

import com.example.kortikov.kodetest.City
import io.reactivex.Observable
import java.util.*

class BookingInteractor {
    fun setDepartureCity (city : City?): Observable<BookingPartialState> {
        return if (city != null) {
            return Observable.just(BookingPartialState.SetDepartureCity(city) as BookingPartialState)
                    .onErrorReturn { error: Throwable -> BookingPartialState.Error(error) }
        } else Observable.just(BookingPartialState.NotChanging())
    }

    fun setArriveCity(city : City?): Observable<BookingPartialState>{
        return if (city != null){
            Observable.just(BookingPartialState.SetArriveCity(city) as BookingPartialState)
                    .onErrorReturn { error: Throwable -> BookingPartialState.Error(error) }
        }
        else Observable.just(BookingPartialState.NotChanging())
    }
    fun setDepartureDate(date: Calendar?): Observable<BookingPartialState>{
        return if (date != null){
            Observable.just(BookingPartialState.SetDepartureDate(date) as BookingPartialState)
                    .onErrorReturn { error: Throwable -> BookingPartialState.Error(error) }
        }
        else Observable.just(BookingPartialState.NotChanging())
    }
    fun setReturnDate(date: Calendar?): Observable<BookingPartialState>{
        return if (date != null){
            Observable.just(BookingPartialState.SetReturnDate(date) as BookingPartialState)
                    .onErrorReturn { error: Throwable -> BookingPartialState.Error(error) }
        }
        else Observable.just(BookingPartialState.NotChanging())
    }
    fun changeAdult(count : Int): Observable<BookingPartialState>{
        return Observable.just(BookingPartialState.ChangeAdultCounter(count))
    }
    fun changeKid(count : Int): Observable<BookingPartialState>{
        return Observable.just(BookingPartialState.ChangeKidCounter(count))
    }
    fun changeBaby(count : Int): Observable<BookingPartialState>{
        return Observable.just(BookingPartialState.ChangeBabyCounter(count))
    }
    fun reverseCities(flag: Boolean): Observable<BookingPartialState>{
        return if (flag) Observable.just(BookingPartialState.ReverseCities())
        else Observable.just(BookingPartialState.NotChanging())
    }
    fun removeReturnDate(flag: Boolean): Observable<BookingPartialState>{
        return if (flag) Observable.just(BookingPartialState.SetReturnDate(null))
        else Observable.just(BookingPartialState.NotChanging())
    }
}
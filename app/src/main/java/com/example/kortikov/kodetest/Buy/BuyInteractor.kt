package com.example.kortikov.kodetest.Buy

import com.example.kortikov.kodetest.City
import io.reactivex.Observable
import java.util.*

class BuyInteractor {
    fun setDepartureCity (city : City?): Observable<BuyPartialState>{
        return if (city != null){
            Observable.just(BuyPartialState(departureCity = city))
                    .onErrorReturn { error: Throwable -> BuyPartialState(error = error) }
        }
        else Observable.just(BuyPartialState())
    }
    fun setArriveCity(city : City?): Observable<BuyPartialState>{
        return if (city != null){
            Observable.just(BuyPartialState(arriveCity = city))
                    .onErrorReturn { error: Throwable -> BuyPartialState(error = error) }
        }
        else Observable.just(BuyPartialState())
    }
    fun setDepartureDate(date: Calendar?): Observable<BuyPartialState>{
        return if (date != null){
            Observable.just(BuyPartialState(dateDeparture = date))
                    .onErrorReturn { error: Throwable -> BuyPartialState(error = error) }
        }
        else Observable.just(BuyPartialState())
    }
    fun setReturnDate(date: Calendar?): Observable<BuyPartialState>{
        return if (date != null){
            Observable.just(BuyPartialState(dateReturn = date))
                    .onErrorReturn { error: Throwable -> BuyPartialState(error = error) }
        }
        else Observable.just(BuyPartialState())
    }
    fun addAdult(count : Int): Observable<BuyPartialState>{
        return Observable.just(BuyPartialState(adultCounter = 1))
    }
    fun removeAdult(count : Int): Observable<BuyPartialState>{
        return Observable.just(BuyPartialState(adultCounter = -1))
    }
    fun addKid(count : Int): Observable<BuyPartialState>{
        return Observable.just(BuyPartialState(kidCounter = 1))
    }
    fun removeKid(count : Int): Observable<BuyPartialState>{
        return Observable.just(BuyPartialState(kidCounter = -1))
    }
    fun addBaby(count : Int): Observable<BuyPartialState>{
        return Observable.just(BuyPartialState(babyCounter = 1))
    }
    fun removeBaby(count : Int): Observable<BuyPartialState>{
        return Observable.just(BuyPartialState(babyCounter = -1))
    }
    fun reverseCities(flag: Boolean): Observable<BuyPartialState>{
        return if (flag) Observable.just(BuyPartialState(isReverse = true))
        else Observable.just(BuyPartialState())
    }
    fun removeReturnDate(flag: Boolean): Observable<BuyPartialState>{
        return if (flag) Observable.just(BuyPartialState(removeReturnDate = true))
        else Observable.just(BuyPartialState())
    }
}
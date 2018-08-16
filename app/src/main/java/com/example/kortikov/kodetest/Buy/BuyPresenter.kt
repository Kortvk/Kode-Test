package com.example.kortikov.kodetest.Buy

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*


class BuyPresenter : MviBasePresenter<BuyView, BuyViewState>() {
    private val buyInteractor = BuyInteractor()

    override fun bindIntents() {
        var setDepartureCity = intent(BuyView::setDepartureCityIntent)
                .flatMap(buyInteractor::setDepartureCity)
                .observeOn(AndroidSchedulers.mainThread())

        var setArriveCity = intent(BuyView::setArriveCityIntent)
                .flatMap(buyInteractor::setArriveCity)
                .observeOn(AndroidSchedulers.mainThread())

        var setDepartureDate = intent(BuyView::setDepartureDateIntent)
                .flatMap(buyInteractor::setDepartureDate)
                .observeOn(AndroidSchedulers.mainThread())

        var setReturnDate = intent(BuyView::setReturnDateIntent)
                .flatMap(buyInteractor::setReturnDate)
                .observeOn(AndroidSchedulers.mainThread())

        var addAdult = intent(BuyView::addAdultIntent)
                .flatMap(buyInteractor::addAdult)
                .observeOn(AndroidSchedulers.mainThread())

        var removeAdult= intent(BuyView::removeAdultIntent)
                .flatMap(buyInteractor::removeAdult)
                .observeOn(AndroidSchedulers.mainThread())

        var addKid= intent(BuyView::addKidIntent)
                .flatMap(buyInteractor::addKid)
                .observeOn(AndroidSchedulers.mainThread())

        var removeKid  = intent(BuyView::removeKidIntent)
                .flatMap(buyInteractor::removeKid)
                .observeOn(AndroidSchedulers.mainThread())

        var addBaby = intent(BuyView::addBabyIntent)
                .flatMap(buyInteractor::addBaby)
                .observeOn(AndroidSchedulers.mainThread())

        var removeBaby = intent(BuyView::removeBabyIntent)
                .flatMap(buyInteractor::removeBaby)
                .observeOn(AndroidSchedulers.mainThread())

        var reverseCities = intent(BuyView::reverseCitiesIntent)
                .flatMap(buyInteractor::reverseCities)
                .observeOn(AndroidSchedulers.mainThread())

        var removeReturnDate = intent(BuyView::removeReturnDateIntent)
                .flatMap(buyInteractor::removeReturnDate)
                .observeOn(AndroidSchedulers.mainThread())

        var allIntents = Observable.merge(listOf(setDepartureCity,
                setArriveCity,
                setDepartureDate,
                setReturnDate,
                addAdult,
                addBaby,
                addKid,
                removeAdult,
                removeBaby,
                removeKid,
                reverseCities,
                removeReturnDate) )
        var initialState = BuyViewState(adultCounter = 1,
                dateDeparture = Calendar.getInstance(),
                dateReturn = Calendar.getInstance(),
                counter = 1)
        var stateObservable = allIntents.scan(initialState, this::viewStateReducer)
        subscribeViewState(stateObservable, BuyView::render)
    }

    private fun viewStateReducer(previousState: BuyViewState, changes: BuyPartialState): BuyViewState {
        previousState.errorText = null
        if (changes.removeReturnDate) {
            previousState.dateReturn = null
            return previousState
        }
        if(changes.isReverse){
            var tmp = previousState.departureCity
            previousState.departureCity = previousState.arriveCity
            previousState.arriveCity = tmp
            return previousState
        }
        if (changes.adultCounter != 0){
            var tmp = previousState.counter + changes.adultCounter
            if (tmp in 1..9 && previousState.adultCounter + changes.adultCounter >= 1) {
                previousState.adultCounter += changes.adultCounter
                previousState.counter += changes.adultCounter
                if (previousState.babyCounter == previousState.adultCounter + 1 && changes.adultCounter < 0) {
                    previousState.babyCounter += changes.adultCounter
                    previousState.counter += changes.adultCounter
                }
            }
            else if (tmp > 9) previousState.errorText = "Пассажиров должно быть не более 9 человек"
            return previousState
        }

        if (changes.kidCounter != 0 && previousState.kidCounter + changes.kidCounter >= 0){
            var tmp = previousState.counter + changes.kidCounter
            if (tmp in 1..9){
                previousState.kidCounter += changes.kidCounter
                previousState.counter += changes.kidCounter
            }
            else if (tmp > 9) previousState.errorText = "Пассажиров должно быть не более 9 человек"
            return previousState
        }

        if (changes.babyCounter != 0){
            var tmp = previousState.counter + changes.babyCounter
            if (tmp in 1..9 && previousState.babyCounter + changes.babyCounter <= previousState.adultCounter && previousState.babyCounter + changes.babyCounter >= 0) {
                previousState.babyCounter += changes.babyCounter
                previousState.counter += changes.babyCounter
            }
            else if (tmp > 9) previousState.errorText = "Пассажиров должно быть не более 9 человек"
            else if (previousState.babyCounter + changes.babyCounter > previousState.adultCounter)
                previousState.errorText = "Младенцев не должно быть больше, чем взрослых"
            return previousState
        }

        if (changes.departureCity != null) {
            if (previousState.arriveCity != null) {
                if (previousState.arriveCity!!.id == changes.departureCity!!.id)
                    previousState.errorText = "Пункты вылета и прилета совпадают"
                else previousState.departureCity = changes.departureCity
            }
            else previousState.departureCity = changes.departureCity
            return previousState
        }

        if (changes.arriveCity != null){
            if (previousState.departureCity != null){
                if (previousState.departureCity!!.id == changes.arriveCity!!.id)
                    previousState.errorText = "Пункты вылета и прилета совпадают"
                else previousState.arriveCity = changes.arriveCity
            }
            else previousState.arriveCity = changes.arriveCity
            return previousState
        }

        if (changes.dateDeparture != null){
            if (previousState.dateReturn!= null && changes.dateDeparture!!.time.time <= previousState.dateReturn!!.time.time){
                previousState.dateDeparture = changes.dateDeparture
                return previousState
            }
        }

        if (changes.dateReturn != null)
            if ( previousState.dateDeparture!!.time.time <= changes.dateReturn!!.time.time){
                previousState.dateReturn = changes.dateReturn
                return previousState
            }

        previousState.dateReturn = null
        return previousState
    }
}
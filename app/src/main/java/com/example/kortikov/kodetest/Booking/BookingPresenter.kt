package com.example.kortikov.kodetest.Booking

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*


class BookingPresenter : MviBasePresenter<BookingView, BookingViewState>() {
    private val buyInteractor = BookingInteractor()

    override fun bindIntents() {
        val setDepartureCity = intent(BookingView::setDepartureCityIntent)
                .flatMap(buyInteractor::setDepartureCity)
                .observeOn(AndroidSchedulers.mainThread())

        val setArriveCity = intent(BookingView::setArriveCityIntent)
                .flatMap(buyInteractor::setArriveCity)
                .observeOn(AndroidSchedulers.mainThread())

        val setDepartureDate = intent(BookingView::setDepartureDateIntent)
                .flatMap(buyInteractor::setDepartureDate)
                .observeOn(AndroidSchedulers.mainThread())

        val setReturnDate = intent(BookingView::setReturnDateIntent)
                .flatMap(buyInteractor::setReturnDate)
                .observeOn(AndroidSchedulers.mainThread())

        val changeAdult = intent(BookingView::changeAdultIntent)
                .flatMap(buyInteractor::changeAdult)
                .observeOn(AndroidSchedulers.mainThread())


        val changeKid= intent(BookingView::changeKidIntent)
                .flatMap(buyInteractor::changeKid)
                .observeOn(AndroidSchedulers.mainThread())

        val changeBaby = intent(BookingView::changeBabyIntent)
                .flatMap(buyInteractor::changeBaby)
                .observeOn(AndroidSchedulers.mainThread())

        val reverseCities = intent(BookingView::reverseCitiesIntent)
                .flatMap(buyInteractor::reverseCities)
                .observeOn(AndroidSchedulers.mainThread())

        val removeReturnDate = intent(BookingView::removeReturnDateIntent)
                .flatMap(buyInteractor::removeReturnDate)
                .observeOn(AndroidSchedulers.mainThread())

        val allIntents = Observable.merge(listOf(setDepartureCity,
                setArriveCity,
                setDepartureDate,
                setReturnDate,
                changeAdult,
                changeBaby,
                changeKid,
                reverseCities,
                removeReturnDate) )

        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_MONTH, 1)
        val dateReturn = date.clone() as Calendar
        dateReturn.add(Calendar.DAY_OF_MONTH, 7)

        var initialState = BookingViewState(adultCounter = 1,
                dateDeparture = date,
                dateReturn = dateReturn,
                counter = 1)
        var stateObservable = allIntents.scan(initialState, this::viewStateReducer)
        subscribeViewState(stateObservable, BookingView::render)
    }

    private fun viewStateReducer(previousState: BookingViewState, changes: BookingPartialState): BookingViewState {
        previousState.errorText = null

        if(changes is BookingPartialState.NotChanging)
            return previousState
        if (changes is BookingPartialState.SetReturnDate) {
            previousState.dateReturn = changes.date
            return previousState
        }
        if(changes is BookingPartialState.ReverseCities){
            val tmp = previousState.departureCity
            previousState.departureCity = previousState.arriveCity
            previousState.arriveCity = tmp
            return previousState
        }
        if (changes is BookingPartialState.ChangeAdultCounter){
            val tmp = previousState.counter + changes.counter
            if (tmp in 1..9 && previousState.adultCounter + changes.counter >= 1) {
                previousState.adultCounter += changes.counter
                previousState.counter += changes.counter
                if (previousState.babyCounter == previousState.adultCounter + 1 && changes.counter < 0) {
                    previousState.babyCounter += changes.counter
                    previousState.counter += changes.counter
                }
            }
            else if (tmp > 9) previousState.errorText = "Пассажиров должно быть не более 9 человек"
            return previousState
        }

        if (changes is BookingPartialState.ChangeKidCounter && previousState.kidCounter + changes.counter >= 0){
            var tmp = previousState.counter + changes.counter
            if (tmp in 1..9){
                previousState.kidCounter += changes.counter
                previousState.counter += changes.counter
            }
            else if (tmp > 9) previousState.errorText = "Пассажиров должно быть не более 9 человек"
            return previousState
        }

        if (changes is BookingPartialState.ChangeBabyCounter){
            var tmp = previousState.counter + changes.counter
            if (tmp in 1..9 && previousState.babyCounter + changes.counter <= previousState.adultCounter && previousState.babyCounter + changes.counter >= 0) {
                previousState.babyCounter += changes.counter
                previousState.counter += changes.counter
            }
            else if (tmp > 9) previousState.errorText = "Пассажиров должно быть не более 9 человек"
            else if (previousState.babyCounter + changes.counter > previousState.adultCounter)
                previousState.errorText = "Младенцев не должно быть больше, чем взрослых"
            return previousState
        }

        if (changes is BookingPartialState.SetDepartureCity) {
            if (previousState.arriveCity != null) {
                if (previousState.arriveCity!!.id == changes.city!!.id)
                    previousState.errorText = "Пункты вылета и прилета совпадают"
                else previousState.departureCity = changes.city
            }
            else previousState.departureCity = changes.city
            return previousState
        }

        if (changes is BookingPartialState.SetArriveCity){
            if (previousState.departureCity != null){
                if (previousState.departureCity!!.id == changes.city!!.id)
                    previousState.errorText = "Пункты вылета и прилета совпадают"
                else previousState.arriveCity = changes.city
            }
            else previousState.arriveCity = changes.city
            return previousState
        }

        if (changes is BookingPartialState.SetDepartureDate){
            if (previousState.dateReturn!= null && changes.date!!.time.time <= previousState.dateReturn!!.time.time){
                previousState.dateDeparture = changes.date
                return previousState
            }
        }

        if (changes is BookingPartialState.SetReturnDate && changes.date != null)
            if ( previousState.dateDeparture!!.time.time <= changes.date!!.time.time){
                previousState.dateReturn = changes.date
                return previousState
            }

        previousState.dateReturn = null
        return previousState
    }
}
package com.example.kortikov.kodetest.Booking

import android.content.Context
import com.example.kortikov.kodetest.R
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class BookingPresenter(val context: Context) : MviBasePresenter<BookingView, BookingViewState>() {

    override fun bindIntents() {
        val setDepartureCity = intent(BookingView::setDepartureCityIntent)
                .flatMap{
                        Observable.just(BookingPartialState.SetDepartureCity(it) as BookingPartialState)
                                .onErrorReturn { error: Throwable -> BookingPartialState.Error(error) }
                }
                .observeOn(AndroidSchedulers.mainThread())

        val setArriveCity = intent(BookingView::setArriveCityIntent)
                .flatMap{
                    Observable.just(BookingPartialState.SetArriveCity(it) as BookingPartialState)
                            .onErrorReturn { error: Throwable -> BookingPartialState.Error(error) }
                }
                .observeOn(AndroidSchedulers.mainThread())

        val setDepartureDate = intent(BookingView::setDepartureDateIntent)
                .flatMap{
                    Observable.just(BookingPartialState.SetDepartureDate(it) as BookingPartialState)
                            .onErrorReturn { error: Throwable -> BookingPartialState.Error(error) }
                }
                .observeOn(AndroidSchedulers.mainThread())

        val setReturnDate = intent(BookingView::setReturnDateIntent)
                .flatMap{
                    Observable.just(BookingPartialState.SetReturnDate(it) as BookingPartialState)
                            .onErrorReturn { error: Throwable -> BookingPartialState.Error(error) }
                }
                .observeOn(AndroidSchedulers.mainThread())

        val changeAdult = intent(BookingView::changeAdultIntent)
                .flatMap{
                    Observable.just(BookingPartialState.ChangeAdultCounter(it))
                }
                .observeOn(AndroidSchedulers.mainThread())


        val changeKid= intent(BookingView::changeKidIntent)
                .flatMap{
                    Observable.just(BookingPartialState.ChangeKidCounter(it))
                }
                .observeOn(AndroidSchedulers.mainThread())

        val changeBaby = intent(BookingView::changeBabyIntent)
                .flatMap{
                    Observable.just(BookingPartialState.ChangeBabyCounter(it))
                }
                .observeOn(AndroidSchedulers.mainThread())

        val reverseCities = intent(BookingView::reverseCitiesIntent)
                .flatMap{
                    Observable.just(BookingPartialState.ReverseCities())
                }
                .observeOn(AndroidSchedulers.mainThread())

        val removeReturnDate = intent(BookingView::removeReturnDateIntent)
                .flatMap{
                    Observable.just(BookingPartialState.SetReturnDate(null))
                }
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
        val dateReturn = Calendar.getInstance()
        dateReturn.add(Calendar.DAY_OF_MONTH, 8)

        val initialState = BookingViewState(adultCounter = 1,
                dateDeparture = date,
                dateReturn = dateReturn,
                counter = 1)
        val stateObservable = allIntents.scan(initialState, this::viewStateReducer)
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
            else if (tmp > 9) previousState.errorText = context.resources.getString(R.string.max_passengers_warning)
            return previousState
        }

        if (changes is BookingPartialState.ChangeKidCounter && previousState.kidCounter + changes.counter >= 0){
            var tmp = previousState.counter + changes.counter
            if (tmp in 1..9){
                previousState.kidCounter += changes.counter
                previousState.counter += changes.counter
            }
            else if (tmp > 9) previousState.errorText = context.resources.getString(R.string.max_passengers_warning)
            return previousState
        }

        if (changes is BookingPartialState.ChangeBabyCounter){
            var tmp = previousState.counter + changes.counter
            if (tmp in 1..9 && previousState.babyCounter + changes.counter <= previousState.adultCounter && previousState.babyCounter + changes.counter >= 0) {
                previousState.babyCounter += changes.counter
                previousState.counter += changes.counter
            }
            else if (tmp > 9) previousState.errorText = context.resources.getString(R.string.max_passengers_warning)
            else if (previousState.babyCounter + changes.counter > previousState.adultCounter)
                previousState.errorText = context.resources.getString(R.string.max_baby_warning)
            return previousState
        }

        if (changes is BookingPartialState.SetDepartureCity) {
            if (previousState.arriveCity != null) {
                if (previousState.arriveCity!!.id == changes.city!!.id)
                    previousState.errorText = context.resources.getString(R.string.departure_arrive_warning)
                else previousState.departureCity = changes.city
            }
            else previousState.departureCity = changes.city
            return previousState
        }

        if (changes is BookingPartialState.SetArriveCity){
            if (previousState.departureCity != null){
                if (previousState.departureCity!!.id == changes.city!!.id)
                    previousState.errorText = context.resources.getString(R.string.departure_arrive_warning)
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
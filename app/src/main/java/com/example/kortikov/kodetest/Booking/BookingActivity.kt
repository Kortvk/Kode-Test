package com.example.kortikov.kodetest.Booking

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kortikov.kodetest.City
import com.example.kortikov.kodetest.R
import com.example.kortikov.kodetest.SearchCity.SearchActivity
import com.example.kortikov.kodetest.Weather.WeatherActivity
import com.hannesdorfmann.mosby3.mvi.MviActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class BookingActivity : MviActivity<BookingView, BookingPresenter>(), BookingView {
    override lateinit var setDepartureCityIntent: Observable<City>
    override lateinit var setArriveCityIntent: Observable<City>
    override lateinit var setDepartureDateIntent: Observable<Calendar>
    override lateinit var setReturnDateIntent: Observable<Calendar>
    override lateinit var changeAdultIntent: Observable<Int>
    override lateinit var changeKidIntent: Observable<Int>
    override lateinit var changeBabyIntent: Observable<Int>
    override lateinit var reverseCitiesIntent: Observable<Boolean>
    override lateinit var removeReturnDateIntent: Observable<Boolean>


    private var departureCityPublishSubject: PublishSubject<City> = PublishSubject.create<City>()
    private var arriveCityPublishSubject: PublishSubject<City> = PublishSubject.create<City>()
    private var departureDatePublishSubject: PublishSubject<Calendar> = PublishSubject.create<Calendar>()
    private var returnDatePublishSubject: PublishSubject<Calendar> = PublishSubject.create<Calendar>()
    private var changeAdultPublishSubject: PublishSubject<Int> = PublishSubject.create<Int>()
    private var changeKidPublishSubject:  PublishSubject<Int> = PublishSubject.create<Int>()
    private var changeBabyPublishSubject:  PublishSubject<Int> = PublishSubject.create<Int>()
    private var reverseCitiesPublishSubject: PublishSubject<Boolean> = PublishSubject.create<Boolean>()
    private var removeReturnDatePublishSubject: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    init {
        setDepartureCityIntent = departureCityPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        setArriveCityIntent = arriveCityPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        setDepartureDateIntent = departureDatePublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        setReturnDateIntent = returnDatePublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        changeAdultIntent = changeAdultPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        changeKidIntent = changeKidPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        changeBabyIntent = changeBabyPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        reverseCitiesIntent = reverseCitiesPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        removeReturnDateIntent = removeReturnDatePublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun createPresenter() = BookingPresenter(applicationContext)

    private var viewState: BookingViewState? = null

    companion object {
        const val APP_ID = "83f63a9aa1765c637d7634976e4621d4"
        const val FLY_FROM_KEY = 0
        const val FLY_TO_KEY = 1

        const val DEPARTURE_CITY_KEY = "departure_city_key"
        const val ARRIVE_CITY_KEY = "arrive_city_key"
        const val DEPARTURE_DATE_KEY = "departure_date_key"
        const val RETURN_DATE_KEY = "return_date_key"
    }



    override fun render(viewState: BookingViewState) {
        this.viewState = viewState

        reverse_btn.isEnabled = !(viewState.arriveCity == null && viewState.departureCity == null)

        adult_text_counter.text = viewState.adultCounter.toString()
        remove_adult_btn.isEnabled = viewState.adultCounter != 1

        baby_text_counter.text = viewState.babyCounter.toString()
        remove_baby_btn.isEnabled = viewState.babyCounter != 0
        baby_text_counter.isEnabled = viewState.babyCounter != 0
        baby_text.isEnabled = viewState.babyCounter != 0
        baby_image.isEnabled = viewState.babyCounter != 0

        kid_text_counter.text = viewState.kidCounter.toString()
        remove_kid_btn.isEnabled = viewState.kidCounter != 0
        kid_text_counter.isEnabled = viewState.kidCounter != 0
        kid_text.isEnabled = viewState.kidCounter != 0
        kid_image.isEnabled = viewState.kidCounter != 0

        if (viewState.departureCity != null){
            city_from_text.text = viewState.departureCity!!.city
            airports_from_text.text = viewState.departureCity!!.airport
        }
        else{
            city_from_text.text = resources.getString(R.string.from_city_string)
            airports_from_text.text = resources.getString(R.string.select_city_secondary)
        }
        if (viewState.arriveCity != null){
            city_to_text.text = viewState.arriveCity!!.city
            airports_to_text.text = viewState.arriveCity!!.airport
        }
        else{
            city_to_text.text = resources.getString(R.string.to_city_string)
            airports_to_text.text = resources.getString(R.string.select_city_secondary)
        }

        if (viewState.errorText != null) {
            Toast.makeText(applicationContext, viewState.errorText, Toast.LENGTH_SHORT).show()
            viewState.errorText = null
        }

        val format = java.text.SimpleDateFormat("dd MMM, EE", Locale("ru"))
        date_to_text.text = format.format(viewState.dateDeparture!!.time).replace(".", "")
        if (viewState.dateReturn != null) {
            date_from_text.text = format.format(viewState.dateReturn!!.time).replace(".", "")

            date_from_btn.visibility = View.GONE

            date_from.visibility = View.VISIBLE
            remove_date_from.visibility = View.VISIBLE
        }
        else{
            date_from_btn.visibility = View.VISIBLE

            date_from.visibility = View.GONE
            remove_date_from.visibility = View.GONE
        }

    }


    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layout_from.setOnClickListener {
            val intent = Intent(applicationContext , SearchActivity::class.java)
            intent.putExtra("request_code", FLY_FROM_KEY)
            startActivityForResult(intent, FLY_FROM_KEY)
        }

        layout_to.setOnClickListener {
            val intent = Intent(applicationContext , SearchActivity::class.java)
            intent.putExtra("request_code", FLY_TO_KEY)
            startActivityForResult(intent, FLY_TO_KEY)
        }

        var date = Calendar.getInstance()
        var currentDate = FLY_FROM_KEY
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            date.set(Calendar.YEAR, year)
            date.set(Calendar.MONTH, monthOfYear)
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            if (currentDate == FLY_FROM_KEY)
                departureDatePublishSubject.onNext(date.clone() as Calendar)
            else
                returnDatePublishSubject.onNext(date.clone() as Calendar)
        }

        date_to.setOnClickListener {
            date = viewState?.dateDeparture ?: Calendar.getInstance()
            currentDate = FLY_FROM_KEY
            val dialog = DatePickerDialog(this, dateSetListener, date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH))
            dialog.datePicker.minDate = Calendar.getInstance().time.time
            dialog.show()
        }
        date_from.setOnClickListener {
            date = viewState?.dateReturn ?: viewState!!.dateDeparture!!.clone() as Calendar
            currentDate = FLY_TO_KEY
            val dialog = DatePickerDialog(this, dateSetListener, date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH))
            dialog.datePicker.minDate = viewState?.dateDeparture?.time?.time ?: Calendar.getInstance().time.time
            dialog.show()
        }

        date_from_btn.setOnClickListener {
            date = viewState?.dateReturn ?: viewState!!.dateDeparture!!.clone() as Calendar
            currentDate = FLY_TO_KEY
            val dialog = DatePickerDialog(this, dateSetListener, date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH))
            dialog.datePicker.minDate = viewState?.dateDeparture?.time?.time ?: Calendar.getInstance().time.time
            dialog.show()
        }

        add_adult_btn.setOnClickListener {
            changeAdultPublishSubject.onNext(1)
        }

        remove_adult_btn.setOnClickListener{
            changeAdultPublishSubject.onNext(-1)
        }

        add_baby_btn.setOnClickListener {
            changeBabyPublishSubject.onNext(1)
        }

        remove_baby_btn.setOnClickListener{
            changeBabyPublishSubject.onNext(-1)
        }

        add_kid_btn.setOnClickListener {
            changeKidPublishSubject.onNext(1)
        }

        remove_kid_btn.setOnClickListener{
            changeKidPublishSubject.onNext(-1)
        }

        btn_next.setOnClickListener {
            if (viewState != null && viewState!!.departureCity != null && viewState!!.arriveCity != null) {
                val intent = Intent(applicationContext, WeatherActivity::class.java)
                intent.putExtra(DEPARTURE_CITY_KEY, viewState!!.departureCity)
                intent.putExtra(ARRIVE_CITY_KEY, viewState!!.arriveCity)
                intent.putExtra(DEPARTURE_DATE_KEY, viewState!!.dateDeparture)
                if(viewState!!.dateReturn != null)
                    intent.putExtra(RETURN_DATE_KEY, viewState!!.dateReturn)
                startActivity(intent)
            }
        }
        reverse_btn.setOnClickListener {
            reverseCitiesPublishSubject.onNext(true)
        }

        remove_date_from.setOnClickListener {
            removeReturnDatePublishSubject.onNext(true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            val city = data!!.getParcelableExtra<City>("city")
            if (requestCode == FLY_FROM_KEY){
                layout_from.post { departureCityPublishSubject.onNext(city) }

            }
            else if (requestCode == FLY_TO_KEY){
                layout_to.post { arriveCityPublishSubject.onNext(city)}
            }
        }
    }
}

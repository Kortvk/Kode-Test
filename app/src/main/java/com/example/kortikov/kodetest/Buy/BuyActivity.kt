package com.example.kortikov.kodetest.Buy

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

class BuyActivity : MviActivity<BuyView, BuyPresenter>(), BuyView {
    override lateinit var removeReturnDateIntent: Observable<Boolean>
    override lateinit var setDepartureCityIntent: Observable<City>
    override lateinit var setArriveCityIntent: Observable<City>
    override lateinit var setDepartureDateIntent: Observable<Calendar>
    override lateinit var setReturnDateIntent: Observable<Calendar>
    override lateinit var addAdultIntent: Observable<Int>
    override lateinit var removeAdultIntent: Observable<Int>
    override lateinit var addKidIntent: Observable<Int>
    override lateinit var removeKidIntent: Observable<Int>
    override lateinit var addBabyIntent: Observable<Int>
    override lateinit var removeBabyIntent: Observable<Int>
    override lateinit var reverseCitiesIntent: Observable<Boolean>


    override fun createPresenter(): BuyPresenter {
        return BuyPresenter()
    }

    private var viewState: BuyViewState? = null

    companion object {
        const val APP_ID = "83f63a9aa1765c637d7634976e4621d4"
        const val FLY_FROM_KEY = 0
        const val FLY_TO_KEY = 1

        const val DEPARTURE_CITY_KEY = "departure_city_key"
        const val ARRIVE_CITY_KEY = "arrive_city_key"
        const val DEPARTURE_DATE_KEY = "departure_date_key"
        const val RETURN_DATE_KEY = "return_date_key"
    }

    override fun render(viewState: BuyViewState) {
        this.viewState = viewState
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

    private lateinit var departureCityPublishSubject: PublishSubject<City>
    private lateinit var arriveCityPublishSubject: PublishSubject<City>

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        departureCityPublishSubject = PublishSubject.create<City>()
        setDepartureCityIntent = departureCityPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())


        arriveCityPublishSubject = PublishSubject.create<City>()
        setArriveCityIntent = arriveCityPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        var departureDatePublishSubject = PublishSubject.create<Calendar>()
        setDepartureDateIntent = departureDatePublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        var returnDatePublishSubject = PublishSubject.create<Calendar>()
        setReturnDateIntent = returnDatePublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        var addAdultPublishSubject = PublishSubject.create<Int>()
        addAdultIntent = addAdultPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        var removeAdultPublishSubject = PublishSubject.create<Int>()
        removeAdultIntent = removeAdultPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        var addKidPublishSubject = PublishSubject.create<Int>()
        addKidIntent = addKidPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        var removeKidPublishSubject = PublishSubject.create<Int>()
        removeKidIntent = removeKidPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        var addBabyPublishSubject = PublishSubject.create<Int>()
        addBabyIntent = addBabyPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        var removeBabyPublishSubject = PublishSubject.create<Int>()
        removeBabyIntent = removeBabyPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        var reverseCitiesPublishSubject = PublishSubject.create<Boolean>()
        reverseCitiesIntent = reverseCitiesPublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        var removeReturnDatePublishSubject = PublishSubject.create<Boolean>()
        removeReturnDateIntent = removeReturnDatePublishSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

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
            if (currentDate == FLY_FROM_KEY) {
                departureDatePublishSubject.onNext(date.clone() as Calendar)
            }
            else{
                returnDatePublishSubject.onNext(date.clone() as Calendar)
            }
        }

        date_to.setOnClickListener {
            date = viewState?.dateDeparture ?: Calendar.getInstance()
            currentDate = FLY_FROM_KEY
            val dialog = DatePickerDialog(this, dateSetListener, date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH))
            dialog.datePicker.minDate = Calendar.getInstance()!!.time.time
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
            addAdultPublishSubject.onNext(1)
        }

        remove_adult_btn.setOnClickListener{
            removeAdultPublishSubject.onNext(1)
        }

        add_baby_btn.setOnClickListener {
            addBabyPublishSubject.onNext(1)
        }

        remove_baby_btn.setOnClickListener{
            removeBabyPublishSubject.onNext(1)
        }

        add_kid_btn.setOnClickListener {
            addKidPublishSubject.onNext(1)
        }

        remove_kid_btn.setOnClickListener{
            removeKidPublishSubject.onNext(1)
        }

        btn_next.setOnClickListener {
            if (viewState != null){
                if (viewState!!.departureCity !=null && viewState!!.arriveCity != null){
                    val intent = Intent(applicationContext, WeatherActivity::class.java)
                    intent.putExtra(DEPARTURE_CITY_KEY, viewState!!.departureCity)
                    intent.putExtra(ARRIVE_CITY_KEY, viewState!!.arriveCity)
                    intent.putExtra(DEPARTURE_DATE_KEY, viewState!!.dateDeparture)
                    if(viewState!!.dateReturn != null)
                        intent.putExtra(RETURN_DATE_KEY, viewState!!.dateReturn)
                    startActivity(intent)
                }
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

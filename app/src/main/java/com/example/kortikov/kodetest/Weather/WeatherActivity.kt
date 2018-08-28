package com.example.kortikov.kodetest.Weather

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.kortikov.kodetest.Booking.BookingActivity
import com.example.kortikov.kodetest.City
import com.example.kortikov.kodetest.R
import com.example.kortikov.kodetest.Weather.List.WeatherController
import com.hannesdorfmann.mosby3.mvi.MviActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : MviActivity<WeatherView, WeatherPresenter>(), WeatherView {
    override lateinit var weatherFromIntent: Observable<City>
    override lateinit var weatherToIntent: Observable<City>

    override fun render(viewState: WeatherViewState) {
        if (viewState is WeatherViewState.WeatherFrom && viewState.result != null) {
            recycler2.layoutManager = LinearLayoutManager(applicationContext)
            val controller = WeatherController()
            recycler2.adapter = controller.adapter
            controller.setData(viewState.result)
        }
        if (viewState is WeatherViewState.WeatherTo && viewState.result != null) {
            recycler1.layoutManager = LinearLayoutManager(applicationContext)
            val controller = WeatherController()
            recycler1.adapter = controller.adapter
            controller.setData(viewState.result)
        }
        if(viewState is WeatherViewState.Error) Toast.makeText(applicationContext, viewState.error.toString(), Toast.LENGTH_LONG).show()
    }

    override fun createPresenter() = WeatherPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.weather_bar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.white)))
        supportActionBar!!.setHomeButtonEnabled(true)
        val actionBarView = supportActionBar!!.customView

        val departureCityText = actionBarView.findViewById<TextView>(R.id.departure_city)
        val arriveCityText = actionBarView.findViewById<TextView>(R.id.arrive_city)
        val backBtn = actionBarView.findViewById<ImageButton>(R.id.back_arrow)

        backBtn.setOnClickListener {
            finish()
        }

        val departureCity = intent.getParcelableExtra<City>(BookingActivity.DEPARTURE_CITY_KEY)
        val arriveCity = intent.getParcelableExtra<City>(BookingActivity.ARRIVE_CITY_KEY)

        departureCityText.text = departureCity.city
        arriveCityText.text = arriveCity.city

        tabhost.setup()
        var tabSpec = tabhost.newTabSpec("tag1")
        tabSpec.setIndicator(layoutInflater.inflate(R.layout.tab_header_to, null))
        tabSpec.setContent(R.id.recycler1)
        tabhost.addTab(tabSpec)

        tabSpec = tabhost.newTabSpec("tag2")
        tabSpec.setIndicator(layoutInflater.inflate(R.layout.tab_header_from, null))
        tabSpec.setContent(R.id.recycler2)
        tabhost.addTab(tabSpec)

        weatherFromIntent = Observable.just(departureCity)
        weatherToIntent = Observable.just(arriveCity)

        tabhost.currentTab = 0

    }
}

package com.example.kortikov.kodetest.Weather

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageButton
import android.widget.TextView
import com.example.kortikov.kodetest.Buy.BuyActivity
import com.example.kortikov.kodetest.City
import com.example.kortikov.kodetest.R
import com.hannesdorfmann.mosby3.mvi.MviActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : MviActivity<WeatherView, WeatherPresenter>(), WeatherView {
    override lateinit var weatherFromIntent: Observable<City>
    override lateinit var weatherToIntent: Observable<City>

    override fun render(viewState: WeatherViewState) {
        if (viewState.weatherFrom != null) {
            recycler2.layoutManager = LinearLayoutManager(applicationContext)
            var adapter = WeatherAdapter()
            adapter.data = viewState.weatherFrom!!
            recycler2.adapter = adapter
        }
        if (viewState.weatherTo != null) {
            recycler1.layoutManager = LinearLayoutManager(applicationContext)
            var adapter = WeatherAdapter()
            adapter.data = viewState.weatherTo!!
            recycler1.adapter = adapter
        }
    }

    override fun createPresenter(): WeatherPresenter {
        return WeatherPresenter()
    }

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

        val departureCity = intent.getParcelableExtra<City>(BuyActivity.DEPARTURE_CITY_KEY)
        val arriveCity = intent.getParcelableExtra<City>(BuyActivity.ARRIVE_CITY_KEY)
        val departureDate = intent.getParcelableExtra<City>(BuyActivity.DEPARTURE_DATE_KEY)
        val returnDate = intent.getParcelableExtra<City>(BuyActivity.RETURN_DATE_KEY)

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

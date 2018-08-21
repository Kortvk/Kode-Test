package com.example.kortikov.kodetest.SearchCity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.kortikov.kodetest.Booking.BookingActivity
import com.example.kortikov.kodetest.City
import com.example.kortikov.kodetest.Epoxy.CityController
import com.example.kortikov.kodetest.R
import com.hannesdorfmann.mosby3.mvi.MviActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import java.lang.Math.pow
import kotlin.math.sqrt

class SearchActivity : MviActivity<SearchView, SearchPresenter>(), SearchView, CityController.ClickCallbacks {
    override fun onBtnClickListener() {
        searchEditText.post { locationPublishSubject.onNext(getSystemService(Context.LOCATION_SERVICE) as LocationManager) }
    }

    override fun onViewClickListener(city: City) {
        val intent = Intent()
        intent.putExtra("city", city)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun createPresenter(): SearchPresenter {
        return SearchPresenter()
    }

    val MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 0

    override lateinit var searchIntent: Observable<String>
    override lateinit var loadIntent: Observable<Boolean>
    override lateinit var locationIntent: Observable<LocationManager>

    private var locationPublishSubject = PublishSubject.create<LocationManager>()

    private var listCity: List<City>? = null
    private var currentLocation: Location? = null

    fun searchCity(location: Location?): City {
        var city = City()
        var min = Double.MAX_VALUE
        for(ct in listCity!!){
            var len = sqrt(pow(location!!.latitude - ct.lat!!, 2.0) + pow(location.longitude - ct.lon!!, 2.0))
            if (len < min){
                min = len
                city = ct
            }
        }
        return city
    }

    override fun render(viewState: SearchViewState) {
        var controller = CityController(this)
        if (viewState is SearchViewState.LoadResult ){

            progress.visibility = View.GONE
            error_layout.visibility = View.GONE
            epoxy_recycler.visibility = View.VISIBLE

            listCity = viewState.result!!

            epoxy_recycler.layoutManager = LinearLayoutManager(applicationContext)
            epoxy_recycler.adapter = controller.adapter

            if (currentLocation != null) controller.setData(listCity, searchCity(currentLocation!!), geo)
            else controller.setData(listCity, null, geo)
        }
        if (viewState is SearchViewState.SearchResult){
            progress.visibility = View.GONE
            error_layout.visibility = View.GONE
            epoxy_recycler.visibility = View.VISIBLE

            listCity = viewState.result!!

            epoxy_recycler.layoutManager = LinearLayoutManager(applicationContext)
            epoxy_recycler.adapter = controller.adapter

            if (currentLocation != null) controller.setData(listCity, searchCity(currentLocation!!), geo)
            else controller.setData(listCity, null, geo)
        }
        if(viewState is SearchViewState.Error){
            progress.visibility = View.GONE
            epoxy_recycler.visibility = View.GONE
            error_layout.visibility = View.VISIBLE
        }
        if(viewState is SearchViewState.Loading){
            progress.visibility = View.VISIBLE
            epoxy_recycler.visibility = View.GONE
            error_layout.visibility = View.GONE
        }
        if (viewState is SearchViewState.Location && viewState.location != null){
            currentLocation = viewState.location!!
            if (listCity != null) {
                controller.setData(listCity, searchCity(currentLocation!!), geo)
            }
        }
    }

    lateinit var searchEditText : EditText
    lateinit var hintText: TextView
    lateinit var backBtn: ImageButton
    var requestCode: Int = -1
    var geo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.search_bar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.white)))
        supportActionBar!!.setHomeButtonEnabled(true)
        val actionBarView = supportActionBar!!.customView

        searchEditText = actionBarView.findViewById<EditText>(R.id.search_edit)
        hintText = actionBarView.findViewById<TextView>(R.id.hint)
        backBtn = actionBarView.findViewById<ImageButton>(R.id.back_arrow)

        var searchPublishSubject = PublishSubject.create<String>()

        searchIntent = searchPublishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        locationIntent = locationPublishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        var loadPublishSubject = PublishSubject.create<Boolean>()
        loadIntent = loadPublishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        searchEditText.post { loadPublishSubject.onNext(true) }

        searchEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchPublishSubject.onNext(p0.toString())
            }

        })


        requestCode = intent.getIntExtra("request_code", 0)

        if (requestCode == BookingActivity.FLY_FROM_KEY)
            setDepartureView()
        else if (requestCode == BookingActivity.FLY_TO_KEY)
            setReturnView()

        backBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        error_btn.setOnClickListener {
            loadPublishSubject.onNext(true)
        }
    }

    private fun setReturnView() {
        searchEditText.hint = resources.getString(R.string.to_city_string)
        hintText.text = resources.getString(R.string.select_destination)

        geo = false
    }

    private fun setDepartureView() {
        searchEditText.hint = resources.getString(R.string.from_city_string)
        hintText.text = resources.getString(R.string.select_departure)

        geo = true

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)
        } else {
            searchEditText.post { locationPublishSubject.onNext(getSystemService(Context.LOCATION_SERVICE) as LocationManager) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            for (i in 0 until permissions.size)
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION){
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        searchEditText.post { locationPublishSubject.onNext(getSystemService(Context.LOCATION_SERVICE) as LocationManager) }
                }
        }
    }


}

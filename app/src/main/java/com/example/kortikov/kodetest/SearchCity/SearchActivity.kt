package com.example.kortikov.kodetest.SearchCity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.example.kortikov.kodetest.Booking.BookingActivity
import com.example.kortikov.kodetest.City
import com.example.kortikov.kodetest.R
import com.example.kortikov.kodetest.SearchCity.List.CityController
import com.hannesdorfmann.mosby3.mvi.MviActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : MviActivity<SearchView, SearchPresenter>(), SearchView, CityController.ClickCallbacks {

    private val MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 0

    override lateinit var searchIntent: Observable<String>
    override lateinit var loadIntent: Observable<Boolean>
    override lateinit var locationIntent: Observable<Boolean>

    private var searchEditText : EditText? = null
    private var hintText: TextView? = null
    private var backBtn: ImageButton? = null
    private var requestCode: Int = -1
    private var geo = CityController.WITHOUT_GEO

    private var locationPublishSubject = PublishSubject.create<Boolean>()

    private var listCity: List<City>? = null
    private var currentLocation: City? = null
    private var listController = CityController(this)

    private fun checkLocationPermission():Boolean{
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return false
        return true
    }

    private fun getLocationPermission(){
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)
    }

    override fun onBtnClickListener() {
        if (checkLocationPermission())
            searchEditText!!.post { locationPublishSubject.onNext(true) }
        else
            getLocationPermission()
    }

    override fun onViewClickListener(city: City) {
        val intent = Intent()
        intent.putExtra("city", city)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun createPresenter(): SearchPresenter {
        return SearchPresenter(applicationContext)
    }

    override fun render(viewState: SearchViewState) {
        if (viewState is SearchViewState.LoadResult ){
            progress.visibility = View.GONE
            error_layout.visibility = View.GONE
            epoxy_recycler.visibility = View.VISIBLE

            listCity = viewState.result!!

            epoxy_recycler.layoutManager = LinearLayoutManager(applicationContext)
            epoxy_recycler.adapter = listController.adapter
            if (currentLocation == null)
                Log.d("location", "null | geo = $geo")
            else
                Log.d("location", "${currentLocation!!.city} | geo = $geo")

            listController.setData(listCity, currentLocation, geo)
        }
        if (viewState is SearchViewState.SearchResult){
            progress.visibility = View.GONE
            error_layout.visibility = View.GONE
            epoxy_recycler.visibility = View.VISIBLE

            listCity = viewState.result!!

            epoxy_recycler.layoutManager = LinearLayoutManager(applicationContext)
            epoxy_recycler.adapter = listController.adapter

            listController.setData(listCity, currentLocation, geo)
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
            currentLocation = viewState.location
            geo = CityController.CITY_FOUND
            if (listCity != null) {
                listController.setData(listCity, currentLocation, geo)
            }
        }
        if (viewState is SearchViewState.LocationRefresh) {
            currentLocation = null
            geo = CityController.SEARCH_CITY
            if (listCity != null) {
                listController.setData(listCity, null, geo)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.search_bar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.white)))
        supportActionBar!!.setHomeButtonEnabled(true)
        val actionBarView = supportActionBar!!.customView

        searchEditText = actionBarView.findViewById(R.id.search_edit)
        hintText = actionBarView.findViewById(R.id.hint)
        backBtn = actionBarView.findViewById(R.id.back_arrow)

        val searchPublishSubject = PublishSubject.create<String>()

        searchIntent = searchPublishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        locationIntent = locationPublishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        val loadPublishSubject = PublishSubject.create<Boolean>()
        loadIntent = loadPublishSubject.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        searchEditText!!.post { loadPublishSubject.onNext(true) }

        searchEditText!!.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.length > 2)
                    searchPublishSubject.onNext(p0.toString())
                else
                    searchPublishSubject.onNext("")
            }
        })

        requestCode = intent.getIntExtra("request_code", 0)

        if (requestCode == BookingActivity.FLY_FROM_KEY)
            setDepartureView()
        else if (requestCode == BookingActivity.FLY_TO_KEY)
            setReturnView()

        backBtn!!.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        error_btn.setOnClickListener {
            loadPublishSubject.onNext(true)
        }
    }

    private fun setReturnView() {
        searchEditText!!.hint = resources.getString(R.string.to_city_string)
        hintText!!.text = resources.getString(R.string.select_destination)

        geo = CityController.WITHOUT_GEO
    }

    private fun setDepartureView() {
        searchEditText!!.hint = resources.getString(R.string.from_city_string)
        hintText!!.text = resources.getString(R.string.select_departure)

        geo = CityController.NO_CITY

        if (!checkLocationPermission()) {
            getLocationPermission()
        } else {
            searchEditText!!.post { locationPublishSubject.onNext(true) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            for (i in 0 until permissions.size)
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION){
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        searchEditText!!.post { locationPublishSubject.onNext(true) }
                    }
                }
        }
    }
}

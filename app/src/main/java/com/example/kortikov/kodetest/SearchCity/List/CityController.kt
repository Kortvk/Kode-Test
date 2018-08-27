package com.example.kortikov.kodetest.SearchCity.List

import com.airbnb.epoxy.Typed3EpoxyController
import com.example.kortikov.kodetest.City

class CityController(var callback: ClickCallbacks) : Typed3EpoxyController<List<City>, City, Int>() {

    companion object {
        const val WITHOUT_GEO = 0
        const val NO_CITY = 1
        const val SEARCH_CITY = 2
        const val CITY_FOUND = 3
    }

    interface ClickCallbacks{
        fun onViewClickListener(city: City)
        fun onBtnClickListener()
    }

    override fun buildModels(cities: List<City>?, ct: City?, geo: Int) {

        when (geo){
            NO_CITY -> {
                cityGeo{
                    id(0)
                    text("Определить мою геопозицию")
                    btnClickListener { _ -> callback.onBtnClickListener() }
                }
            }
            SEARCH_CITY ->{
                cityGeo{
                    id(0)
                    text("Определяем вашу геопозицию...")
                    btnClickListener { _ -> callback.onBtnClickListener() }
                }
            }
            CITY_FOUND -> {
                cityGeo{
                    id(ct!!.id)
                    text(ct.city)
                    airports(ct.airport)
                    clickListener { _ -> callback.onViewClickListener(ct) }
                    btnClickListener { _ -> callback.onBtnClickListener() }
                }
            }
        }

        cities!!.forEach {
            city {
                id(it.id)
                text(it.city)
                airports(it.airport)
                clickListener { t -> callback.onViewClickListener(it)}
            }
        }

        if (geo > 0){
            if (ct != null){

            }
            else{

            }
        }


    }
}
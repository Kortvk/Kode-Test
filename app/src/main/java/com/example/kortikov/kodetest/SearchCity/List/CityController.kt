package com.example.kortikov.kodetest.SearchCity.List

import com.airbnb.epoxy.Typed3EpoxyController
import com.example.kortikov.kodetest.City

class CityController(var callback: ClickCallbacks) : Typed3EpoxyController<List<City>, City, Boolean>() {
    interface ClickCallbacks{
        fun onViewClickListener(city: City)
        fun onBtnClickListener()
    }

    override fun buildModels(cities: List<City>?, ct: City?, geo: Boolean) {
        if (geo){
            if (ct != null){
                cityGeo{
                    id(ct.id)
                    text(ct.city)
                    airports(ct.airport)
                    clickListener { t -> callback.onViewClickListener(ct) }
                    btnClickListener { t -> callback.onBtnClickListener() }
                }
            }
            else{
                cityGeo{
                    id(0)
                    text("Определить мою геопозицию")
                    btnClickListener {t -> callback.onBtnClickListener() }
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
    }
}
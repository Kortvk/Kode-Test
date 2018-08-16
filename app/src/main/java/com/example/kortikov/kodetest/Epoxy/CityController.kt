package com.example.kortikov.kodetest.Epoxy

import android.view.View
import android.widget.Toast
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed2EpoxyController
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
                    clickListener { t -> callback.onViewClickListener(ct) }
                    btnClickListener { t -> callback.onBtnClickListener() }
                }
            }
            else{
                cityGeo{
                    id(0)
                    text("Определить ваше местоположение")
                    btnClickListener {t -> callback.onBtnClickListener() }
                }
            }
        }

        cities!!.forEach {
            city {
                id(it.id)
                text(it.city)
                clickListener { t -> callback.onViewClickListener(it)}
            }
        }
    }
}
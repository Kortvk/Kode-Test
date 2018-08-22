package com.example.kortikov.kodetest.Weather.List

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.example.kortikov.kodetest.WeatherList
import java.text.SimpleDateFormat
import java.util.*

class WeatherController: TypedEpoxyController<WeatherList>() {

    override fun buildModels(data: WeatherList?) {
        for (i in 0 until data!!.list!!.size){
            val format = SimpleDateFormat("dd MMM\nHH:mm", Locale("ru"))
            val date = Date(data.list!![i].dt!! * 1000L)
            weather {
                id(i)
                dateTime(format.format(date))
                tempText(data.list!![i].main!!.temp!!.toInt().toString() + "°C")
                imageUrl(data.list!![i].weather!![0].icon)
            }
        }
    }
}
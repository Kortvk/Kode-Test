package com.example.kortikov.kodetest.Weather.List

import android.support.annotation.LongDef
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.kortikov.kodetest.R
import com.squareup.picasso.Picasso

@EpoxyModelClass(layout = R.layout.weather_card)
abstract class WeatherModel : EpoxyModelWithHolder<WeatherModel.Holder>(){

    @EpoxyAttribute @StringRes
    var dateTime: String? = null
    @EpoxyAttribute @StringRes
    var tempText: String? = null
    @EpoxyAttribute @StringRes
    var imageUrl: String? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.dateTime!!.text = dateTime
        holder.tempText!!.text = tempText
        Picasso.get().load("""http://openweathermap.org/img/w/$imageUrl.png""").into(holder.image)
    }

    class Holder : EpoxyHolder() {

        var image: ImageView? = null
        var tempText: TextView? = null
        var dateTime: TextView? = null

        override fun bindView(view: View) {
            image = view.findViewById(R.id.image)
            tempText = view.findViewById(R.id.temp_text)
            dateTime = view.findViewById(R.id.date_time_text)
        }
    }
}
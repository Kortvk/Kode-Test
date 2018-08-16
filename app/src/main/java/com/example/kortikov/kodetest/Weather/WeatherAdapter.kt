package com.example.kortikov.kodetest.Weather

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.kortikov.kodetest.R
import com.example.kortikov.kodetest.WeatherList
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    var data = WeatherList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.weather_card, parent, false))
    }

    override fun getItemCount(): Int {
        return data.list!!.size
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var format = SimpleDateFormat("dd MMM\nHH:mm", Locale("ru"))
        var date = Date(data.list!![position].dt!! * 1000L)
        holder.dateTime.text = format.format(date)
        holder.tempText.text = data.list!![position].main!!.temp!!.toInt().toString() + "°C"
        Picasso.get().load("""http://openweathermap.org/img/w/${data.list!![position].weather!![0].icon}.png""").into(holder.image)
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var image: ImageView = view.findViewById(R.id.image)
        var tempText: TextView = view.findViewById(R.id.temp_text)
        var dateTime: TextView = view.findViewById(R.id.date_time_text)
    }
}
package com.example.kortikov.kodetest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherList {
    @SerializedName("list")
    @Expose
    var list: kotlin.collections.List<List>? = null

    override fun toString(): String {
        var result = ""
        for (wt in list!!)
            result += wt.toString() + " | "
        return result
    }

    inner class List{
        @SerializedName("dt")
        @Expose
        var dt: Long? = null

        @SerializedName("main")
        @Expose
        var main : Main? = null

        @SerializedName("weather")
        @Expose
        var weather : kotlin.collections.List<Weather>? = null

        override fun toString(): String {
            return weather!![0].description!!
        }

        inner class Weather {
            @SerializedName("main")
            @Expose
            private val main: String? = null
            @SerializedName("description")
            @Expose
            val description: String? = null
            @SerializedName("icon")
            @Expose
            val icon: String? = null
        }

        inner class Main {
            @SerializedName("temp")
            @Expose
            var temp: Double? = null
            @SerializedName("pressure")
            @Expose
            private var pressure: Double? = null
        }
    }
}
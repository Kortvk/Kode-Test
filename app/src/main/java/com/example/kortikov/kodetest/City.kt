package com.example.kortikov.kodetest

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class City() : Parcelable, Comparator<City> {
    constructor(city : String):this(){
        this.city = city
        this.airport = "Все аэропорты"
    }

    @SerializedName("zip")
    @Expose
    var zip: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("localized_country_name")
    @Expose
    var localizedCountryName: String? = null

    @SerializedName("distance")
    @Expose
    var distance: Double? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("lon")
    @Expose
    var lon: Double? = null

    @SerializedName("ranking")
    @Expose
    var ranking: Int? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("member_count")
    @Expose
    var memberCount: Int? = null

    @SerializedName("lat")
    @Expose
    var lat: Double? = null
    
    var airport: String = "Все аэропорты"

    constructor(parcel: Parcel) : this() {
        zip = parcel.readString()
        country = parcel.readString()
        localizedCountryName = parcel.readString()
        distance = parcel.readValue(Double::class.java.classLoader) as? Double
        city = parcel.readString()
        lon = parcel.readValue(Double::class.java.classLoader) as? Double
        ranking = parcel.readValue(Int::class.java.classLoader) as? Int
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        memberCount = parcel.readValue(Int::class.java.classLoader) as? Int
        lat = parcel.readValue(Double::class.java.classLoader) as? Double
    }

    constructor(city:String, lon:Double, lat:Double) : this() {
        this.city = city
        this.lon = lon
        this.lat = lat
    }

    override fun compare(p0: City?, p1: City?): Int {
        return p0!!.id!!.compareTo(p1!!.id!!)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(zip)
        parcel.writeString(country)
        parcel.writeString(localizedCountryName)
        parcel.writeValue(distance)
        parcel.writeString(city)
        parcel.writeValue(lon)
        parcel.writeValue(ranking)
        parcel.writeValue(id)
        parcel.writeValue(memberCount)
        parcel.writeValue(lat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<City> {
        override fun createFromParcel(parcel: Parcel): City {
            return City(parcel)
        }

        override fun newArray(size: Int): Array<City?> {
            return arrayOfNulls(size)
        }
    }

}
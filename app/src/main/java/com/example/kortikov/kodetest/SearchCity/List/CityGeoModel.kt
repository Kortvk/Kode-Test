package com.example.kortikov.kodetest.SearchCity.List

import android.support.annotation.StringRes
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.kortikov.kodetest.R

@EpoxyModelClass(layout = R.layout.geo_view)
abstract class CityGeoModel: EpoxyModelWithHolder<CityGeoModel.Holder>() {
    @EpoxyAttribute
    @StringRes
    var text: String? = null

    @EpoxyAttribute
    @StringRes
    var airports: String? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: View.OnClickListener? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var btnClickListener: View.OnClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.text!!.text = text
        if(airports != null) {
            holder.airports!!.text = airports
            holder.view!!.isClickable = true
            holder.view!!.isFocusable = true
            holder.view!!.setOnClickListener(clickListener)
        }
        else
            holder.airports!!.visibility = View.GONE

        holder.btn!!.setOnClickListener(btnClickListener)
    }

    class Holder: EpoxyHolder(){
        var text: TextView? = null
        var airports: TextView? = null
        var btn: ImageButton? = null
        var view: View? = null
        override fun bindView(itemView: View) {
            text = itemView.findViewById(R.id.geo_name_text)
            airports = itemView.findViewById(R.id.geo_airports_text)
            btn = itemView.findViewById(R.id.geo_btn)
            view = itemView
        }

    }
}
package com.example.kortikov.kodetest.SearchCity.List

import android.support.annotation.StringRes
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.kortikov.kodetest.R

@EpoxyModelClass(layout = R.layout.view_city)
abstract class CityModel : EpoxyModelWithHolder<CityModel.Holder>() {
    @EpoxyAttribute @StringRes
    var text: String? = null

    @EpoxyAttribute @StringRes
    var airports: String? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: View.OnClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.text!!.text = text
        holder.airports!!.text = airports

        holder.view!!.setOnClickListener(clickListener)
    }

    class Holder: EpoxyHolder(){
        var text: TextView? = null
        var airports: TextView? = null
        var view: View? = null
        override fun bindView(itemView: View) {
            text = itemView.findViewById(R.id.title_text)
            view = itemView
            airports = itemView.findViewById(R.id.airports_text)
        }

    }
}
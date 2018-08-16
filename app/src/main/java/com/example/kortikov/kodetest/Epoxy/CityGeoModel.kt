package com.example.kortikov.kodetest.Epoxy

import android.support.annotation.StringRes
import android.view.View
import android.widget.Button
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

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: View.OnClickListener? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var btnClickListener: View.OnClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.text!!.text = text

        holder.text!!.setOnClickListener(clickListener)
        holder.btn!!.setOnClickListener(btnClickListener)
    }

    class Holder: EpoxyHolder(){
        var text: TextView? = null
        var btn: ImageButton? = null
        var view: View? = null
        override fun bindView(itemView: View) {
            text = itemView.findViewById(R.id.geo_name_text)
            btn = itemView.findViewById(R.id.geo_btn)
            view = itemView
        }

    }
}
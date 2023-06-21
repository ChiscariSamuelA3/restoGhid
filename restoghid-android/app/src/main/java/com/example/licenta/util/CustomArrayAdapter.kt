package com.example.licenta.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.licenta.ui.restaurants.RestaurantItemViewModel
import java.util.*
import kotlin.collections.ArrayList

class CustomArrayAdapter(context: Context, resource: Int, objects: List<RestaurantItemViewModel>) :
    ArrayAdapter<RestaurantItemViewModel>(context, resource, objects) {

    private val restaurantsList: List<RestaurantItemViewModel> = ArrayList(objects)

    private val filter: Filter = CustomFilter()

    override fun getFilter() = filter

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val restaurant = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = restaurant?.name
        return view
    }

    private inner class CustomFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint == null || constraint.isEmpty()) {
                results.values = emptyList<RestaurantItemViewModel>()
                results.count = 0
            } else {
                val suggestions = ArrayList<RestaurantItemViewModel>()
                val filterPattern = constraint.toString().lowercase(Locale.getDefault()).trim()
                for (item in restaurantsList) {
                    if (item.name.lowercase(Locale.getDefault()).contains(filterPattern)) {
                        suggestions.add(item)
                    }
                }

                results.values = suggestions
                results.count = suggestions.size
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            if (results?.count ?: 0 > 0) {
                addAll(results?.values as List<RestaurantItemViewModel>)
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as RestaurantItemViewModel).name
        }
    }
}

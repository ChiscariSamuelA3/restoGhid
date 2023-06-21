package com.example.licenta.ui.restaurants

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta.R
import com.example.licenta.models.Filter

class RestaurantsAdapter(val viewModel: RestaurantsViewModel) :
    RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {
    private var filters: List<Filter> = ArrayList()
    private var checkedStates = SparseBooleanArray()

    fun setFilters(filters: List<Filter>) {
        this.filters = filters

        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox_filter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filters[position]
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = filter.isChecked
        holder.checkBox.text = filter.name

        if (position < 4) {
            holder.checkBox.setBackgroundResource(R.drawable.custom_checkbox)
        } else {
            holder.checkBox.setBackgroundResource(R.drawable.default_checkbox)
        }

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            filter.isChecked = isChecked
            viewModel.setFilters(filters)

//            notifyItemChanged(position)
        }
    }


    override fun getItemCount(): Int = filters.size

    fun getSelectedFilters(): List<Filter> = filters.filter { it.isChecked }
}

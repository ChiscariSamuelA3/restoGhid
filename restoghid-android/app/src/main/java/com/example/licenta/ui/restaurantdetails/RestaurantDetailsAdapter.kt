package com.example.licenta.ui.restaurantdetails

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta.R
import com.example.licenta.models.Cuisine
import com.example.licenta.models.DietaryRestrictions


class RestaurantDetailsAdapter(val type: Int) :
    RecyclerView.Adapter<RestaurantDetailsAdapter.RestaurantDetailsViewHolder>() {
    private var cuisineList: List<Cuisine> = ArrayList()
    private var dietaryList: List<DietaryRestrictions> = ArrayList()

    fun setCuisineList(cuisineList: List<Cuisine>) {
        this.cuisineList = cuisineList

        // refresh recycler view for new data
        notifyDataSetChanged()
    }

    fun setDietaryList(dietaryList: List<DietaryRestrictions>) {
        this.dietaryList = dietaryList

        Log.d("_CUISINE","here")

        // refresh recycler view for new data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantDetailsViewHolder {

        if(type == 0) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_cuisine, parent, false)
            return RestaurantDetailsViewHolder(view)
        }
        else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_dietary, parent, false)
            return RestaurantDetailsViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RestaurantDetailsViewHolder, position: Int) {
        if(type == 0) {
            holder.bindCuisine(cuisineList[position])
        }
        else {
            holder.bindDietary(dietaryList[position])
        }
    }

    override fun getItemCount(): Int {
        return if(type == 0) {
            cuisineList.size
        } else {
            dietaryList.size
        }
    }

    class RestaurantDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindCuisine(cuisine: Cuisine) {
            val tvCuisine: TextView = itemView.findViewById(R.id.tv_cuisine)
            tvCuisine.text = cuisine.name
        }

        fun bindDietary(dietary: DietaryRestrictions) {
            val tvDietary: TextView = itemView.findViewById(R.id.tv_dietary)
            tvDietary.text = dietary.name
        }
    }
}

package com.geek.newyorkrestaurant.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.geek.newyorkrestaurant.databinding.ListItemViewBinding
import com.geek.newyorkrestaurant.model.Restaurant


class RestaurantHolder(private val binding: ListItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindValues(restaurant: Restaurant){
        binding.tvName.text = restaurant.name
        binding.tvCuisine.text = restaurant.cuisine
        binding.tvStreet.text = restaurant.address?.street
    }

}
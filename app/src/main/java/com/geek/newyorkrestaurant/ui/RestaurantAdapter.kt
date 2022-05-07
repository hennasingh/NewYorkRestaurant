package com.geek.newyorkrestaurant.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.geek.newyorkrestaurant.R
import com.geek.newyorkrestaurant.databinding.ListItemViewBinding
import com.geek.newyorkrestaurant.model.Restaurant
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter


class RestaurantAdapter(data: OrderedRealmCollection<Restaurant>):RealmRecyclerViewAdapter<Restaurant, RestaurantHolder>(data, true)   {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHolder {
        val binding = ListItemViewBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return RestaurantHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantHolder, position: Int) {
       val restaurant = getItem(position)
        holder.bindValues(restaurant!!)

    }
}

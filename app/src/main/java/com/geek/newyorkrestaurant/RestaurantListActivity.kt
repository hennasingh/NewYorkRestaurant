package com.geek.newyorkrestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber

class RestaurantListActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        val extras = intent.getStringExtra("FLAG")

        title = if(extras.equals("location")){
            val location = intent.getStringExtra("LOCATION")
            location!!
        }else {
            val cuisine = intent.getStringExtra("CUISINE")
            cuisine!!
        }

        Timber.d("partition is $extras")
    }
}
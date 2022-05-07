package com.geek.newyorkrestaurant.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geek.newyorkrestaurant.R
import com.geek.newyorkrestaurant.databinding.ActivityRestaurantListBinding
import com.geek.newyorkrestaurant.model.Restaurant
import com.geek.newyorkrestaurant.newYorkApp
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import io.realm.mongodb.sync.Sync
import io.realm.mongodb.sync.SyncConfiguration
import timber.log.Timber
import java.io.File

class RestaurantListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.getStringExtra("FLAG")

        Timber.d("Setting Realm instance again")
        val config = SyncConfiguration.Builder(newYorkApp.currentUser()!!, "NewYork")
            .build()

        if(extras.equals("location")){
            val location = intent.getStringExtra("LOCATION")
            title =  location!!
            displayRestaurantAt(location, config)
            
        }else {
            val cuisine = intent.getStringExtra("CUISINE")
           title = cuisine!!
            displayRestaurantWith(cuisine, config)
        }

        Timber.d("partition is $extras")
        recyclerView = binding.rvList
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun displayRestaurantWith(cuisine: String, config: SyncConfiguration) {

        Realm.getInstanceAsync(config, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {

                val restaurantList: RealmResults<Restaurant> = realm.where<Restaurant>()
                    .equalTo("cuisine", cuisine).findAll()
                updateUI(restaurantList)
            }
        })
    }

    private fun displayRestaurantAt(location: String, config: SyncConfiguration) {

        Realm.getInstanceAsync(config, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {

                val restaurantList: RealmResults<Restaurant> = realm.where<Restaurant>()
                    .equalTo("borough", location).findAll()
                updateUI(restaurantList)
            }
        })
    }

    private fun updateUI(restaurantList: RealmResults<Restaurant>) {

        Timber.d("Synced restaurants count ${restaurantList.size}")
        binding.progress.visibility = View.GONE

        //Timber.d(restaurantList.asJSON())

        adapter = RestaurantAdapter(restaurantList)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}
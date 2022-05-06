package com.geek.newyorkrestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.geek.newyorkrestaurant.databinding.ActivityNewyorkBinding
import com.geek.newyorkrestaurant.model.Restaurant
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import timber.log.Timber
import java.io.File

class NewYorkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewyorkBinding
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_newyork)

        user = newYorkApp.currentUser()
    }

    override fun onStart() {
        super.onStart()
        searchLocations()
        searchCuisines()
    }

    private fun searchCuisines() {
        val config = SyncConfiguration.Builder(user!!, "NewYork")
            .waitForInitialRemoteData()
            .build()

        Timber.d("Opening Realm instance Asynchronously")
        /**
         * Should print false if its opening for the first time
         */
        Timber.d("${File(config.path).exists()}")

        Realm.getInstanceAsync(config, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {

                val allData: RealmResults<Restaurant> = realm.where<Restaurant>().distinct("cuisine").findAll()
                displayCuisineDropdown(allData)
            }
        })
    }

    private fun displayCuisineDropdown(allData: RealmResults<Restaurant>) {
        Timber.d("Synced Cuisine count ${allData.size}")
        binding.progress.visibility = View.GONE
        val list = ArrayList<String?>()

        allData.forEach{
            list.add(it.cuisine)
        }
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_cuisine, list)
        binding.autoCompleteCuisine.setAdapter(arrayAdapter)

        binding.autoCompleteCuisine.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val intent = Intent(applicationContext, RestaurantListActivity::class.java)
                intent.putExtra("CUISINE",value.toString()!!)
                intent.putExtra("FLAG","cuisine")
                startActivity(intent)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun searchLocations() {

        val config = SyncConfiguration.Builder(user!!, "NewYork")
            .waitForInitialRemoteData()
            .build()

        Timber.d("Opening Realm instance Asynchronously")
        /**
         * Should print false if its opening for the first time
         */
        Timber.d("${File(config.path).exists()}")

        Realm.getInstanceAsync(config, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {

                val allData: RealmResults<Restaurant> = realm.where<Restaurant>().distinct("borough").findAll()
                displayLocationDropdown(allData)

            }
        })
    }

    private fun displayLocationDropdown(allData: RealmResults<Restaurant>) {
        Timber.d("Synced Location count ${allData.size}")
        binding.progress.visibility = View.GONE
        val list = ArrayList<String?>()

        allData.forEach{
            list.add(it.borough)
        }
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_location, list)
        binding.autoCompleteLocation.setAdapter(arrayAdapter)

        binding.autoCompleteLocation.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val intent = Intent(applicationContext, RestaurantListActivity::class.java)
                intent.putExtra("LOCATION",value.toString()!!)
                intent.putExtra("FLAG","location")
                startActivity(intent)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
}
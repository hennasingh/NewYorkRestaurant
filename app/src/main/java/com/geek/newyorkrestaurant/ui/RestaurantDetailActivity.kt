package com.geek.newyorkrestaurant.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.geek.newyorkrestaurant.databinding.ActivityRestaurantDetailBinding
import com.geek.newyorkrestaurant.model.Restaurant
import com.geek.newyorkrestaurant.model.Reviews
import com.geek.newyorkrestaurant.newYorkApp
import io.realm.*

import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import timber.log.Timber
import java.lang.StringBuilder

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var binding :ActivityRestaurantDetailBinding
    private lateinit var config:SyncConfiguration
    private var restID: String? = null
    private var stringBuilder: StringBuilder = StringBuilder("")
    private lateinit var reviewRealm: Realm

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

         restID = intent.getStringExtra("RESTID")

         Timber.d("Setting Realm instance again")
        config = SyncConfiguration.Builder(newYorkApp.currentUser()!!, "NewYork")
            .build()

        binding.addReviewBtn.setOnClickListener{
            val input = EditText(this)
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Add a review for this restaurant:")
                .setCancelable(true)
                .setPositiveButton("Add"){dialog, _->
                    val review = input.text.toString()
                    dialog.dismiss()
                    addReviewToDatabase(review)
                }
                .setNegativeButton("Cancel"){dialog, _ ->
                    dialog.cancel()
                }
            val dialog = dialogBuilder.create()
            dialog.setView(input)
            dialog.setTitle("Add Restaurant Review")
            dialog.show()
        }
        recyclerView = binding.rvReviews
    }

    private fun addReviewToDatabase(userReview: String) {


        reviewRealm.executeTransactionAsync { bgRealm ->
            val review = Reviews(
                review = userReview,
                userid = newYorkApp.currentUser()!!.id,
                username = newYorkApp.currentUser()!!.profile.email
            )
            val restaurant =
                bgRealm.where<Restaurant>().equalTo("restaurant_id", restID).findFirst()
            restaurant!!.reviews.add(review)

        }
    }

    override fun onStart() {
        super.onStart()
        stringBuilder.clear()
        getRestDetail(config)
    }

    private fun getRestDetail(config: SyncConfiguration) {

        reviewRealm = Realm.getInstance(config)
        Realm.getInstanceAsync(config, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {

              val restaurant = realm.where<Restaurant>()
                    .equalTo("restaurant_id", restID).findFirst()
                title = restaurant?.name
                binding.tvName.text = restaurant?.name
                 stringBuilder.append(restaurant?.address?.building).append(" ").append(restaurant?.address?.street).append(" ").append(restaurant?.address?.zipcode)
                binding.tvAddress.text = stringBuilder

                if(restaurant?.reviews?.size==0){
                    binding.tvNoReview.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                }else {
                    displayReviewAdapter(restaurant)
                }
                callChangeListener(restaurant)

            }
        })

    }

    private fun callChangeListener(restaurant: Restaurant?) {

        val listener = OrderedRealmCollectionChangeListener{ collection: RealmList<Reviews>?, changeSet: OrderedCollectionChangeSet? ->
           for(range in changeSet!!.insertionRanges){
               if(restaurant?.reviews?.size==0){
                   binding.tvNoReview.visibility = View.VISIBLE
                   binding.progress.visibility = View.GONE
               }else {
                   displayReviewAdapter(restaurant)
               }
           }
        }
        restaurant!!.reviews.addChangeListener(listener)
    }

    private fun displayReviewAdapter(restaurant: Restaurant?) {
        adapter = ReviewsAdapter(restaurant!!.reviews)
        binding.tvNoReview.visibility = View.GONE
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.progress.visibility = View.GONE

        recyclerView.layoutManager = LinearLayoutManager(this@RestaurantDetailActivity)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this@RestaurantDetailActivity, DividerItemDecoration.VERTICAL))
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView.adapter = null
        // if a user hasn't logged out when the activity exits, still need to explicitly close the realm
        reviewRealm.close()
    }
}
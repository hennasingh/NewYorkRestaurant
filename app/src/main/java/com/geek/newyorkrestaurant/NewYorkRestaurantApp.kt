package com.geek.newyorkrestaurant

import android.app.Application
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.sync.SyncConfiguration
import timber.log.Timber

const val appId = "newyorkrestaurants-ruyhm"
lateinit var newYorkApp: App

class NewYorkRestaurantApp : Application(){

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        Timber.plant(Timber.DebugTree())

        newYorkApp = App(AppConfiguration.Builder(appId).build())
    }


}
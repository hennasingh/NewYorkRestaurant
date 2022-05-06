package com.geek.newyorkrestaurant.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Restaurant(

    @PrimaryKey
    var _id: ObjectId? = null,
    var _partition: String = "",
    var address: RestaurantAddress? = null,
    var borough: String? = null,
    var cuisine: String? = null,
    var name: String? = null,
    var restaurant_id: String? = null
): RealmObject() {}

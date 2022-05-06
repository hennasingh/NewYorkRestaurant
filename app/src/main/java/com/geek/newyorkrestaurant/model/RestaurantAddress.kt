package com.geek.newyorkrestaurant.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass(embedded = true)
open class RestaurantAddress(

        var building: String? = null,
        @Required
        var coord: RealmList<Double> = RealmList(),
        var street: String? = null,
        var zipcode: String? = null
): RealmObject() {}



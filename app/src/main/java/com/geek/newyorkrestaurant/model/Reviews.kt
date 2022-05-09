package com.geek.newyorkrestaurant.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass(embedded = true)
open class Reviews(
    var review: String? = null,
    var userid: String? = null,
    var username: String? = null
): RealmObject() {}
package com.geek.newyorkrestaurant.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geek.newyorkrestaurant.databinding.ListItemViewBinding
import com.geek.newyorkrestaurant.databinding.ListReviewItemBinding
import com.geek.newyorkrestaurant.model.Restaurant
import com.geek.newyorkrestaurant.model.Reviews
import io.realm.OrderedRealmCollection
import io.realm.RealmList
import io.realm.RealmRecyclerViewAdapter

class ReviewsAdapter(dataSet: RealmList<Reviews>): RealmRecyclerViewAdapter<Reviews,ReviewHolder?>(dataSet, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val binding = ListReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ReviewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.bindValues(getItem(position)!!)
    }
}
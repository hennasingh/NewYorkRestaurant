package com.geek.newyorkrestaurant.ui

import androidx.recyclerview.widget.RecyclerView
import com.geek.newyorkrestaurant.databinding.ListReviewItemBinding
import com.geek.newyorkrestaurant.model.Restaurant
import com.geek.newyorkrestaurant.model.Reviews

class ReviewHolder(private val binding: ListReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindValues(review: Reviews){

            binding.tvUsername.text = review.username
            binding.tvReview.text = review.review
        }
    }
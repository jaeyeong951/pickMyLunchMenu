package com.example.pickmylunchmenu.ui.review

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pickmylunchmenu.databinding.ReviewItemBinding
import com.toy.plzPickMyLunchMenu.domain.dto.Review

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewListViewHolder>() {
    var reviewList = emptyList<Review>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ReviewListViewHolder(val binding: ReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListViewHolder {
        return ReviewListViewHolder(ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ReviewListViewHolder, position: Int) {
        with(holder) {
            with(reviewList[position]) {
                Log.e("review", position.toString())
                binding.reviewProfilePhoto.load(profile_photo_url)
                binding.reviewProfileRatingStar.rating = rating.toFloat()
                binding.reviewProfileRating.text = rating.toFloat().toString()
                binding.reviewProfileDays.text = relative_time_description
                binding.reviewProfileName.text = author_name
                binding.reviewContent.text = text
            }
        }
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

}
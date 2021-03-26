package com.example.pickmylunchmenu.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pickmylunchmenu.databinding.RestaurantItemBinding
import com.example.pickmylunchmenu.dto.NearByRestaurantItem

class RestaurantListAdapter(private val listener: (View, Int) -> Unit) : RecyclerView.Adapter<RestaurantListAdapter.RestaurantListViewHolder>() {
    var restaurantList = emptyList<NearByRestaurantItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class RestaurantListViewHolder(val binding: RestaurantItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantListViewHolder {
        return RestaurantListViewHolder(RestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(holder: RestaurantListViewHolder, position: Int) {
        with(holder) {
            with(restaurantList[position]) {
                binding.restaurantTitle.text = name
                binding.restaurantAddress.text = doro ?: jibun
                binding.restaurantReviewAvg.text = "${rating.toString()} (${user_ratings_total})"
                binding.restaurantReviewTelephoneNum.text = tel ?: "전화번호가 없어요"
                binding.root.setOnClickListener { listener(binding.root, position) }
            }
        }
    }
}
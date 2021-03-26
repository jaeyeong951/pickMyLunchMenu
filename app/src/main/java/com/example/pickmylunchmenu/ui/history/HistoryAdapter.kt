package com.example.pickmylunchmenu.ui.history

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pickmylunchmenu.databinding.HistoryItemBinding
import com.example.pickmylunchmenu.dto.HistoryDto

class HistoryAdapter(private val longClickListener: (View, Int) -> Unit) : RecyclerView.Adapter<HistoryAdapter.HistoryListViewHolder>() {
    var reviewList = emptyList<HistoryDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class HistoryListViewHolder(val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryListViewHolder {
        return HistoryListViewHolder(HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryListViewHolder, position: Int) {
        with(holder) {
            with(reviewList[position]) {
                Log.e("review", position.toString())
                binding.historyItemDate.text = date
                binding.historyItemRestaurantName.text = name
                binding.historyMyComment.text = comment
                binding.historyStar.rating = myRating
                binding.root.setOnLongClickListener {
                    longClickListener(binding.root, position)
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

}
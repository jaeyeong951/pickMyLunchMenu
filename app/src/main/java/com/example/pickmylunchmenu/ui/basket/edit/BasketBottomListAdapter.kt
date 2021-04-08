package com.example.pickmylunchmenu.ui.basket.edit

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.pickmylunchmenu.databinding.BasketMenuItemBinding
import com.example.pickmylunchmenu.dto.OrderDetailDto

class BasketBottomListAdapter(private val basketEditViewModel: BasketEditViewModel) : RecyclerView.Adapter<BasketBottomListAdapter.BasketBottomListViewHolder>() {

    var detailList = emptyList<OrderDetailDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class BasketBottomListViewHolder(val binding: BasketMenuItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketBottomListViewHolder {
        return BasketBottomListViewHolder(BasketMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: BasketBottomListViewHolder, position: Int) {
        with(holder) {
            with(detailList[position]) {
                binding.basketMenuItemTitle.text = menuName
                "(${count})".also { binding.basketMenuItemQuantity.text = it }
                "${price * count}원".also { binding.basketMenuItemPrice.text = it }
            }
        }
    }

    override fun getItemCount(): Int {
        return detailList.size
    }
}
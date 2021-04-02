package com.example.pickmylunchmenu.ui.basket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pickmylunchmenu.databinding.BasketItemBinding
import com.example.pickmylunchmenu.dto.OrderDetailDto
import com.example.pickmylunchmenu.dto.OrderDto

class BasketAdapter(val viewModel: BasketViewModel) : RecyclerView.Adapter<BasketAdapter.BasketListViewHolder>() {
    var basketList = emptyList<OrderDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class BasketListViewHolder(val binding: BasketItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketListViewHolder {
        return BasketListViewHolder(
            BasketItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BasketListViewHolder, position: Int) {
        with(holder) {
            with(basketList[position]) {
                var totalPrice = 0
                var listString = ""
                binding.basketItemTitle.text = restaurant.name
                orderDetailList.forEach { detail ->
                    listString += "${detail.menuName}(${detail.count})\n"
                    totalPrice += (detail.count * detail.price)
                }
                binding.basketItemList.text = listString
                "${totalPrice}원".also { binding.basketItemTotalPrice.text = it }
                binding.basketItemDeleteButton.setOnClickListener {
                    viewModel.deleteOrder(id)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return basketList.size
    }
}
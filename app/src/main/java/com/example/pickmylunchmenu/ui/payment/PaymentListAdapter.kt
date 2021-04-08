package com.example.pickmylunchmenu.ui.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pickmylunchmenu.databinding.ItemPaymentOrderBinding
import com.example.pickmylunchmenu.dto.OrderDto

class PaymentListAdapter : RecyclerView.Adapter<PaymentListAdapter.PaymentListViewHolder>() {
    var orderList = emptyList<OrderDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class PaymentListViewHolder(val binding: ItemPaymentOrderBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentListViewHolder {
        return PaymentListViewHolder(
            ItemPaymentOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PaymentListViewHolder, position: Int) {
        with(holder) {
            with(orderList[position]) {
                var menuList = ""
                var priceList = ""
                var totalPrice = 0
                orderDetailList.forEach {
                    menuList += "${it.menuName}(${it.count})\n"
                    priceList += "${it.price*it.count}원\n"
                    totalPrice += it.price * it.count
                }
                binding.itemPaymentOrderTitle.text = restaurant.name
                binding.itemPaymentOrderTitlePrice.text = "${totalPrice}원"
                binding.itemPaymentOrderListText.text = menuList
                binding.itemPaymentOrderPriceListText.text = priceList
            }
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}
package com.example.pickmylunchmenu.ui.basket.edit

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.pickmylunchmenu.databinding.ItemMenuCheckBinding
import com.example.pickmylunchmenu.dto.MenuDto
import com.example.pickmylunchmenu.dto.OrderDetailDto

class BasketEditMenuListAdapter(private val basketEditViewModel: BasketEditViewModel) : RecyclerView.Adapter<BasketEditMenuListAdapter.BasketEditMenuListViewHolder>() {

    var menuList = emptyList<MenuDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var totalPrice = 0
        set(value) {
            field = value
            basketEditViewModel.setTotalPrice(value)
        }

    inner class BasketEditMenuListViewHolder(val binding: ItemMenuCheckBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketEditMenuListViewHolder {
        return BasketEditMenuListViewHolder(ItemMenuCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: BasketEditMenuListViewHolder, position: Int) {
        with(holder) {
            with(menuList[position]) {
                binding.itemMenuTitle.text = menuName
                (price.toString() + "원").also { binding.itemMenuPrice.text = it }
                binding.root.setOnClickListener {
                    binding.itemMenuCheckbox.isChecked = !binding.itemMenuCheckbox.isChecked
                }
                binding.itemMenuCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    when(isChecked) {
                        true -> {
                            binding.itemMenuController.visibility = View.VISIBLE
                            if(basketEditViewModel.selectedBasket!!.orderDetailList.find { it.menu_id == id } == null) {
                                basketEditViewModel.selectedBasket!!.orderDetailList.add(
                                    OrderDetailDto(
                                        id = 0,
                                        menuName = menuName,
                                        price = price,
                                        count = binding.itemMenuControllerQuantity.text.toString().toInt(),
                                        menu_id = id
                                    )
                                )
                            }
                            totalPrice += this.price * binding.itemMenuControllerQuantity.text.toString().toInt()
                        }
                        false -> {
                            binding.itemMenuController.visibility = View.GONE
                            basketEditViewModel.selectedBasket!!.orderDetailList.removeIf {
                                it.menuName == this.menuName
                            }
                            totalPrice -= this.price * binding.itemMenuControllerQuantity.text.toString().toInt()
                        }
                    }
                }
                basketEditViewModel.selectedBasket!!.orderDetailList.forEach {
                    if(it.menu_id == id) {
                        binding.itemMenuCheckbox.isChecked = true
                        binding.itemMenuControllerQuantity.text = it.count.toString()
                    }
                }
                binding.itemMenuControllerPlusButton.setOnClickListener {
                    binding.itemMenuControllerQuantity.text = (binding.itemMenuControllerQuantity.text.toString().toInt() + 1).toString()
                    basketEditViewModel.selectedBasket!!.orderDetailList.find {
                        it.menu_id == this.id
                    }!!.count++
                    totalPrice += this.price
                }
                binding.itemMenuControllerMinusButton.setOnClickListener {
                    if(binding.itemMenuControllerQuantity.text != "1") {
                        binding.itemMenuControllerQuantity.text = (binding.itemMenuControllerQuantity.text.toString().toInt() - 1).toString()
                        basketEditViewModel.selectedBasket!!.orderDetailList.find {
                            it.menu_id == this.id
                        }!!.count--
                        totalPrice -= this.price
                    }
                    else {
                        // do nothing
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

}
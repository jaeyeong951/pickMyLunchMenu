package com.example.pickmylunchmenu.ui.restaurant

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.databinding.ItemMenuCheckBinding
import com.example.pickmylunchmenu.dto.MenuDto
import com.example.pickmylunchmenu.dto.OrderMenuDto

class RestaurantMenuListAdapter(private val activityViewModel: MainActivityViewModel) : RecyclerView.Adapter<RestaurantMenuListAdapter.MenuListViewHolder>() {

    var menuList = emptyList<MenuDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var totalPrice = 0
        set(value) {
            field = value
            activityViewModel.setTotalPrice(value)
        }

    inner class MenuListViewHolder(val binding: ItemMenuCheckBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        return MenuListViewHolder(ItemMenuCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        with(holder) {
            with(menuList[position]) {
                Log.e("review", position.toString())
                binding.itemMenuTitle.text = menuName
                (price.toString() + "원").also { binding.itemMenuPrice.text = it }
                binding.root.setOnClickListener {
                    binding.itemMenuCheckbox.isChecked = !binding.itemMenuCheckbox.isChecked
                }
                binding.itemMenuCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    when(isChecked) {
                        true -> {
                            binding.itemMenuController.visibility = View.VISIBLE
                            activityViewModel.orderMenuList.add(OrderMenuDto(this.id, binding.itemMenuControllerQuantity.text.toString().toInt()))
                            totalPrice += this.price * binding.itemMenuControllerQuantity.text.toString().toInt()
                        }
                        false -> {
                            binding.itemMenuController.visibility = View.GONE
                            activityViewModel.orderMenuList.removeIf {
                                it.id == this.id
                            }
                            totalPrice -= this.price * binding.itemMenuControllerQuantity.text.toString().toInt()
                        }
                    }
                }
                binding.itemMenuControllerPlusButton.setOnClickListener {
                    binding.itemMenuControllerQuantity.text = (binding.itemMenuControllerQuantity.text.toString().toInt() + 1).toString()
                    activityViewModel.orderMenuList.find {
                        it.id == this.id
                    }!!.quantity++
                    totalPrice += this.price
                }
                binding.itemMenuControllerMinusButton.setOnClickListener {
                    if(binding.itemMenuControllerQuantity.text != "1") {
                        binding.itemMenuControllerQuantity.text = (binding.itemMenuControllerQuantity.text.toString().toInt() - 1).toString()
                        activityViewModel.orderMenuList.find {
                            it.id == this.id
                        }!!.quantity--
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
package com.example.pickmylunchmenu.ui.basket

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.R
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentBasketBinding
import com.example.pickmylunchmenu.dto.OrderDto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasketFragment : BaseFragment<FragmentBasketBinding, BasketViewModel>() {
    override val viewModel: BasketViewModel by viewModels()

    val activityViewModel: MainActivityViewModel by activityViewModels()

    var height = 0

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val params = _binding!!.basketToolbar.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height + 48

        _binding!!.basketPurchaseButton.setOnClickListener {
            findNavController().navigate(R.id.action_basketFragment_to_paymentFragment)
        }

        viewModel.getOrders(activityViewModel.cachedUser!!.uid)

        viewModel.isGetOrdersFinished.observe(viewLifecycleOwner, {
            var price = 0
            if(viewModel.orderDtoList.isEmpty()) {
                _binding!!.basketEmptyIcon.visibility = View.VISIBLE
                _binding!!.basketEmptyText.visibility = View.VISIBLE
                _binding!!.basketList.visibility = View.GONE
                _binding!!.basketPurchaseButton.visibility = View.GONE
                _binding!!.basketPriceContainerPrice.visibility = View.GONE
                _binding!!.basketPriceContainerTitle.visibility = View.GONE
            }
            else {
                _binding!!.basketEmptyIcon.visibility = View.GONE
                _binding!!.basketEmptyText.visibility = View.GONE
                _binding!!.basketList.visibility = View.VISIBLE
                _binding!!.basketPurchaseButton.visibility = View.VISIBLE
                _binding!!.basketPriceContainerPrice.visibility = View.VISIBLE
                _binding!!.basketPriceContainerTitle.visibility = View.VISIBLE
                _binding!!.basketList.adapter = BasketAdapter(viewModel = viewModel, listener = { _, position ->
                    Log.e("onCLicked", viewModel.orderDtoList.reversed()[position].restaurant.name )
                    val bundle = bundleOf("basket" to viewModel.orderDtoList.reversed()[position])
                    findNavController().navigate(R.id.action_basketFragment_to_basketEditFragment, bundle)
                }).apply {
                    basketList = ArrayList(viewModel.orderDtoList.reversed())
                    activityViewModel.orderDtoList = basketList
                    Log.e("장바구니 불러오기", "성공")
                }
                viewModel.orderDtoList.forEach { order ->
                    order.orderDetailList.forEach { detail ->
                        price += (detail.price * detail.count)
                    }
                }
                activityViewModel.priceForPurchase = price
                "${price}원".also { _binding!!.basketPriceContainerPrice.text = it }
            }

        })

        viewModel.isGetOrdersFailed.observe(viewLifecycleOwner, {
            Log.e("장바구니 불러오기", "실패")
        })
    }
}
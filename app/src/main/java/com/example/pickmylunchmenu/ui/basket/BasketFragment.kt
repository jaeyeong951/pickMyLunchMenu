package com.example.pickmylunchmenu.ui.basket

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentBasketBinding
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

        viewModel.getOrders(activityViewModel.cachedUser!!.uid)

        viewModel.isGetOrdersFinished.observe(viewLifecycleOwner, {
            var price = 0
            _binding!!.basketList.adapter = BasketAdapter(viewModel).apply {
                basketList = viewModel.orderDtoList.reversed()
                Log.e("장바구니 불러오기", "성공")
            }
            viewModel.orderDtoList.forEach { order ->
                order.orderDetailList.forEach { detail ->
                    price += (detail.price * detail.count)
                }
            }
            "${price}원".also { _binding!!.basketPriceContainerPrice.text = it }
        })

        viewModel.isGetOrdersFailed.observe(viewLifecycleOwner, {
            Log.e("장바구니 불러오기", "실패")
        })
    }
}
package com.example.pickmylunchmenu.ui.basket.edit

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentBasketEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasketEditFragment : BaseFragment<FragmentBasketEditBinding, BasketEditViewModel>() {
    override val viewModel: BasketEditViewModel by viewModels()

    val activityViewModel: MainActivityViewModel by activityViewModels()

    var height = 0

    private val args: BasketEditFragmentArgs by navArgs()

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentBasketEditBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val params = _binding!!.basketEditToolbar.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height + 48

        viewModel.selectedBasket = args.basket

        Log.e("BasketEditFragment", viewModel.selectedBasket.toString())

        _binding!!.apply {
            viewModel.selectedBasket!!.apply {
                basketEditRestaurantTitle.text = restaurant.name
                basketEditRestaurantCategory.text = restaurant.category
                basketEditRestaurantAddress.text = restaurant.doro ?: restaurant.jibun
                basketEditRestaurantReviewAvg.text = restaurant.rating.toString()
                basketEditRestaurantTelephoneNum.text = restaurant.tel ?: "전화번호가 제공되지 않는 식당입니다."

                basketEditRestaurantMyOrderList.adapter = BasketEditMenuListAdapter(viewModel).apply {
                    menuList = viewModel.selectedBasket!!.restaurant.menus
                }

                basketEditRestaurantEditButton.setOnClickListener {
                    viewModel.submitUpdate()
                }
            }
            viewModel.totalPrice.observe(viewLifecycleOwner, { price ->
                ("변경하기(${price}원)").also { basketEditRestaurantEditButton.text = it }
            })
        }
        viewModel.isUpdateFinished.observe(viewLifecycleOwner, {
            findNavController().popBackStack()
        })
        viewModel.isUpdateFailed.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "메뉴 변경에 실패했습니다.", Toast.LENGTH_SHORT).show()
        })
    }
}
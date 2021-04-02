package com.example.pickmylunchmenu.ui.welcome

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.R
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding, WelcomeViewModel>() {
    override val viewModel: WelcomeViewModel by viewModels()

    val activityViewModel: MainActivityViewModel by activityViewModels()

    private var height = 0

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val params = _binding!!.welcomeToolbar.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height + 48

        (activityViewModel.cachedUser!!.userId + "님 환영합니다.").also { _binding!!.welcomeTitle.text = it }

        _binding!!.welcomeKorean.setOnClickListener {
            viewModel.getRestaurantByCategory("한식")
        }
        _binding!!.welcomeJapanese.setOnClickListener {
            viewModel.getRestaurantByCategory("일식")
        }
        _binding!!.welcomeChinese.setOnClickListener {
            viewModel.getRestaurantByCategory("중식")
        }
        _binding!!.welcomeWestern.setOnClickListener {
            viewModel.getRestaurantByCategory("양식")
        }
        _binding!!.toolbarBasket.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_basketFragment)
        }
        viewModel.isLoadRestaurantFinished.observe(viewLifecycleOwner, {
            activityViewModel.cachedRestaurantList = viewModel.restaurantList
            activityViewModel.cachedRestaurantList.forEach {
                Log.e("restaurant name : ", it.name)
                Log.e("restaurant category : ", it.category)
            }
            findNavController().navigate(R.id.action_welcomeFragment_to_mainTwoFragment)
        })
    }
}
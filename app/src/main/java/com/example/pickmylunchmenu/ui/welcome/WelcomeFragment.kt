package com.example.pickmylunchmenu.ui.welcome

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.R
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentWelcomeBinding
import com.example.pickmylunchmenu.util.fadeIn
import com.example.pickmylunchmenu.util.fadeOutInvisible
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding, WelcomeViewModel>() {
    override val viewModel: WelcomeViewModel by viewModels()

    val activityViewModel: MainActivityViewModel by activityViewModels()

    private var height = 0

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val params = _binding!!.welcomeToolbar.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height + 48

        viewModel.getDelayedPayments(activityViewModel.cachedUser!!.uid)

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
        _binding!!.welcomeDelayedPaymentsButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_paymentFragment)
        }
        viewModel.isLoadRestaurantFinished.observe(viewLifecycleOwner, {
            activityViewModel.cachedRestaurantList = viewModel.restaurantList
            activityViewModel.cachedRestaurantList.forEach {
                Log.e("restaurant name : ", it.name)
                Log.e("restaurant category : ", it.category)
            }
            findNavController().navigate(R.id.action_welcomeFragment_to_mainTwoFragment)
        })
        viewModel.isGetDelayedPaymentsFinished.observe(viewLifecycleOwner, {
            activityViewModel.delayedPayments = viewModel.delayedPayments
            if(viewModel.delayedPayments.isEmpty()) {
                Log.e("나중 결제", "없어용")
                _binding!!.welcomeDelayedPaymentsButton.fadeOutInvisible()
            }
            else {
                Log.e("나중 결제", viewModel.delayedPayments.toString())
                _binding!!.welcomeDelayedPaymentsButton.fadeIn()
                val delayedDate = LocalDate.parse(viewModel.delayedPayments[0].date.substringBefore("T"))
                val orderDate = delayedDate.minusDays(7)
                val remainingDays = LocalDate.now(ZoneId.of("Asia/Seoul")).until(delayedDate).days
                activityViewModel.remainingDays = remainingDays
                ("${orderDate.year}년 ${orderDate.monthValue}월 ${orderDate.dayOfMonth}일 주문 결제하기\n" +
                        "(${remainingDays}일 후 만료)")
                    .also { _binding!!.welcomeDelayedPaymentsButton.text = it }
            }
        })
    }
}
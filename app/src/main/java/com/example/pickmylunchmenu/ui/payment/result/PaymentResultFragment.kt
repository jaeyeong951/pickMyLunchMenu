package com.example.pickmylunchmenu.ui.payment.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentPaymentResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentResultFragment : BaseFragment<FragmentPaymentResultBinding, PaymentResultViewModel>() {

    override val viewModel: PaymentResultViewModel by viewModels()

    val activityViewModel: MainActivityViewModel by activityViewModels()

    val args: PaymentResultFragmentArgs by navArgs()

    var height = 0

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentPaymentResultBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val params = _binding!!.paymentResultToolbar.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height + 48

        _binding!!.apply {
            var totalQuantity = 0
            var totalPrice = 0
            args.paymentResult.orderList.forEach {
                it.orderDetailList.forEach { detail ->
                    totalQuantity += detail.count
                    totalPrice += detail.count * detail.price
                }
            }
            "${args.paymentResult.orderList[0].orderDetailList[0].menuName} 외 ${totalQuantity - 1}개".also { paymentResultMenuDesc.text = it }
            "${totalPrice}원".also { paymentResultPriceDesc.text = it }
            if(args.paymentResult.method == null) {
                paymentResultMethodDesc.text = "나중결제"
                "1주 이내로 결제를 완료해주세요.\n (${args.paymentResult.date.substringBefore("T")}까지)".also { paymentResultDateDesc.text = it }
            }
            else {
                paymentResultMethodDesc.text = args.paymentResult.method
                paymentResultDateDesc.text = args.paymentResult.date.substringBefore("T")
            }
            "${activityViewModel.cachedUser!!.userId}님의 결제내역".also { paymentResultTitle.text = it }
            paymentResultBackButton.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.clearOrder()
    }
}
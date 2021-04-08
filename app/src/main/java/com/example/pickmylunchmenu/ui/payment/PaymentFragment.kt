package com.example.pickmylunchmenu.ui.payment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.R
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : BaseFragment<FragmentPaymentBinding, PaymentViewModel>() {

    override val viewModel: PaymentViewModel by viewModels()

    val activityViewModel: MainActivityViewModel by activityViewModels()

    var height = 0

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val params = _binding!!.paymentToolbar.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height + 48

        _binding!!.apply {
            if(findNavController().previousBackStackEntry?.destination!!.id == R.id.welcomeFragment) {
                paymentCancel.visibility = View.VISIBLE
                paymentRadioNajung.visibility = View.GONE
                paymentRadioJigum.text = "지금 결제하기 (${activityViewModel.remainingDays}일 후 만료)"
                var priceForPurchase = 0
                activityViewModel.delayedPayments[0].orderList.forEach {
                    it.orderDetailList.forEach { detail ->
                        priceForPurchase += detail.count * detail.price
                    }
                }
                "${priceForPurchase}원".also { paymentTotalPrice.text = it }
                paymentOrderListRecyclerView.adapter = PaymentListAdapter().apply {
                    this.orderList = activityViewModel.delayedPayments[0].orderList
                }
            }
            else {
                paymentOrderListRecyclerView.adapter = PaymentListAdapter().apply {
                    this.orderList = activityViewModel.orderDtoList
                    "${activityViewModel.priceForPurchase}원".also { paymentTotalPrice.text = it }
                }
            }
            paymentRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when(checkedId) {
                    R.id.payment_radio_jigum -> {
                        _binding!!.paymentRadioGroupJigum.visibility = View.VISIBLE
                    }
                    R.id.payment_radio_najung -> {
                        _binding!!.paymentRadioGroupJigum.visibility = View.GONE
                    }
                    R.id.payment_cancel -> {
                        _binding!!.paymentRadioGroupJigum.visibility = View.GONE
                    }
                }
            }
            paymentPurchaseButton.setOnClickListener {
                if(findNavController().previousBackStackEntry?.destination!!.id == R.id.basketFragment) {
                    val orders = activityViewModel.orderDtoList
                    when(paymentRadioGroup.checkedRadioButtonId) {
                        R.id.payment_radio_jigum -> {
                            when(paymentRadioGroupJigum.checkedRadioButtonId) {
                                R.id.payment_radio_card -> {
                                    viewModel.payment(orders.map { it.id }, 0)
                                }
                                R.id.payment_radio_realtime -> {
                                    viewModel.payment(orders.map { it.id }, 1)
                                }
                                R.id.payment_radio_virtual -> {
                                    viewModel.payment(orders.map { it.id }, 2)
                                }
                                else -> {
                                    Toast.makeText(requireContext(), "결제방식을 선택해주세요!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        R.id.payment_radio_najung -> {
                            if(activityViewModel.delayedPayments.isEmpty()) {
                                viewModel.payment(orders.map { it.id }, null)
                            }
                            else {
                                Toast.makeText(requireContext(), "미결제 내역이 있습니다.\n미결제 내역 정산 후 결제를 진행해 주세요!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else -> {
                            Toast.makeText(requireContext(), "결제방식을 선택해주세요!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    when(paymentRadioGroup.checkedRadioButtonId) {
                        R.id.payment_cancel -> {
                            viewModel.cancelDelayedPayment(activityViewModel.delayedPayments[0].id)
                        }
                        R.id.payment_radio_jigum -> {
                            when(paymentRadioGroupJigum.checkedRadioButtonId) {
                                R.id.payment_radio_card -> {
                                    viewModel.proceedDelayedPayment(activityViewModel.delayedPayments[0].id, 0)
                                }
                                R.id.payment_radio_realtime -> {
                                    viewModel.proceedDelayedPayment(activityViewModel.delayedPayments[0].id, 1)
                                }
                                R.id.payment_radio_virtual -> {
                                    viewModel.proceedDelayedPayment(activityViewModel.delayedPayments[0].id, 1)
                                }
                                else -> {
                                    Toast.makeText(requireContext(), "결제방식을 선택해주세요!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else -> {
                            Toast.makeText(requireContext(), "결제방식을 선택해주세요!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        viewModel.isPaymentCancelFinished.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "결제가 취소 되었습니다.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_paymentFragment_pop_including_basketFragment)
        })

        viewModel.isPaymentFinished.observe(viewLifecycleOwner, {
//            activityViewModel.clearOrder()
//            findNavController().navigate(R.id.action_paymentFragment_pop_including_basketFragment)
            val bundle =
                bundleOf("fromBasket" to (findNavController().previousBackStackEntry?.destination!!.id == R.id.basketFragment),
                    "paymentResult" to viewModel.paymentDTO)
            findNavController().navigate(R.id.action_paymentFragment_to_paymentResultFragment, bundle)
        })

        viewModel.isPaymentFailed.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "결제 실패!", Toast.LENGTH_SHORT).show()
        })
    }
}
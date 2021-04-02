package com.example.pickmylunchmenu.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.R
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginVIewModel>() {

    override val viewModel: LoginVIewModel by viewModels()

    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private var height : Int = 0

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val params = _binding!!.loginIdTitle.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height + 48

        _binding!!.loginProceedButton.setOnClickListener {
            val userId = _binding!!.loginIdEdit.text.toString()
            val password = _binding!!.loginPasswordEdit.text.toString()
            if(userId.isEmpty() || userId.isBlank()) Toast.makeText(requireContext(), "아이디를 입력해주세요!", Toast.LENGTH_SHORT).show()
            else if(password.isBlank() || password.isEmpty()) Toast.makeText(requireContext(), "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
            else viewModel.login(userId, password)
        }

        viewModel.isLoginFinished.observe(viewLifecycleOwner, {
            activityViewModel.cachedUser = viewModel.user
            findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
        })

        viewModel.isLoginFailed.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "로그인에 실패했습니다. \n 아이디와 비밀번호를 다시 확인해주세요!", Toast.LENGTH_SHORT).show()
        })
    }
}
package com.startwithn.exchange_android.ui.page.login.forgot.verify_phone_number

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.FragmentForgotPasswordBinding
import com.startwithn.exchange_android.ui.page.base.BaseFragment
import com.startwithn.exchange_android.ui.page.login.LoginFragmentDirections

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>(R.layout.fragment_forgot_password) {
    companion object {
        fun newInstance() = ForgotPasswordFragment()
        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            fadeIn()
        }
    }

    override fun setUp() {

    }

    override fun listener() {

    }

    private fun fadeIn() {
        with(binding) {
            //llLogin.slideUp()
        }
    }



}
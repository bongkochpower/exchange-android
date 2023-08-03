package com.powersoftlab.exchange_android.ui.page.login.forgot.verify_phone_number

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.FragmentForgotPasswordBinding
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.login.LoginFragmentDirections

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
        binding.apply {
            isVerifyPhone = true
            isPhoneEmpty = false
        }
    }

    override fun listener() {
        binding.apply {
            btnConfirmTel.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    if (isValidateForm()) {
                        gotoVerifyOtp()
                    }
                }
            }

            edtPhoneNumber.doAfterTextChanged {
                if(it.toString().length > 1){
                    isPhoneEmpty = false
                }
            }
        }

    }

    private fun fadeIn() {
        with(binding) {
            //llLogin.slideUp()
        }
    }

    private fun isValidateForm(): Boolean {
        var isValidate = false
        with(binding) {
            val phone = edtPhoneNumber.text.toString()

            when {
                phone.isEmpty() -> {
                    isValidate = false
                    isPhoneEmpty = true
                }
                else -> {
                    isValidate = true
                }
            }
        }
        return isValidate

    }

    private fun gotoVerifyOtp(){
        binding.apply {
            isVerifyPhone = false
        }
    }


}
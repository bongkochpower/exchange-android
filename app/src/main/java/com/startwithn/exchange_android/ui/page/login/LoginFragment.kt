package com.startwithn.exchange_android.ui.page.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.FragmentLoginBinding
import com.startwithn.exchange_android.ui.page.base.BaseFragment

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            fadeIn()
        }
    }
    override fun setUp() {

    }

    private fun fadeIn() {
        with(binding) {
            //llLogin.slideUp()
        }
    }

}
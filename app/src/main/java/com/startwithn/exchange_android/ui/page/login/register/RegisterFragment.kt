package com.startwithn.exchange_android.ui.page.login.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.FragmentLoginBinding
import com.startwithn.exchange_android.databinding.FragmentRegisterBinding
import com.startwithn.exchange_android.ext.fadeIn
import com.startwithn.exchange_android.ext.slideUp
import com.startwithn.exchange_android.ui.page.base.BaseFragment
import com.startwithn.exchange_android.ui.page.login.LoginFragment
import com.startwithn.exchange_android.ui.page.login.LoginFragmentDirections

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {
    companion object {
        fun newInstance() = RegisterFragment()

        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(TermRegisterFragmentDirections.actionTermRegisterFragmentToRegisterFragment())
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
            llRegisterForm.fadeIn()
        }
    }

}
package com.startwithn.exchange_android.ui.page.login.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.FragmentRegisterBinding
import com.startwithn.exchange_android.databinding.FragmentTermRegisterBinding
import com.startwithn.exchange_android.ui.page.base.BaseFragment
import com.startwithn.exchange_android.ui.page.login.LoginFragmentDirections

class TermRegisterFragment : BaseFragment<FragmentTermRegisterBinding>(R.layout.fragment_term_register) {
    companion object {
        fun newInstance() = TermRegisterFragment()

        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTermRegisterFragment())
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
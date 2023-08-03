package com.powersoftlab.exchange_android.ui.page.login.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.databinding.FragmentTermRegisterBinding
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.login.LoginFragmentDirections
import com.powersoftlab.exchange_android.ui.page.login.LoginViewModel
import com.powersoftlab.exchange_android.ui.page.login.register.register.RegisterFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TermRegisterFragment : BaseFragment<FragmentTermRegisterBinding>(R.layout.fragment_term_register) {

    private val loginViewModel: LoginViewModel by sharedViewModel()

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
        loginViewModel.setIcon(R.drawable.icon_back)
    }

    override fun listener() {
        with(binding){
            btnRegister.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    if(isValidateTerm()){
                        RegisterFragment.navigate(this@TermRegisterFragment)
                    }else{
                        val msg = resources.getString(R.string.message_valid_term_and_con)
                        AppAlert.alert(requireContext(),msg).show(childFragmentManager)
                    }
                }
            }
        }
    }


    private fun fadeIn() {
        with(binding) {
            //llLogin.slideUp()
        }
    }
    private fun isValidateTerm() : Boolean{
        return binding.chkTerm.isChecked
    }

}
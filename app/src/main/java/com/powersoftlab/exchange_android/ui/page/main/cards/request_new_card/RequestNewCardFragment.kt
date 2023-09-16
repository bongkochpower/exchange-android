package com.powersoftlab.exchange_android.ui.page.main.cards.request_new_card

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.FragmentCardsBinding
import com.powersoftlab.exchange_android.databinding.FragmentLoginBinding
import com.powersoftlab.exchange_android.databinding.FragmentRequestNewCardBinding
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.login.LoginFragmentDirections
import com.powersoftlab.exchange_android.ui.page.login.register.register.RegisterFragment
import com.powersoftlab.exchange_android.ui.page.main.cards.CardsFragmentDirections

class RequestNewCardFragment : BaseFragment<FragmentRequestNewCardBinding>(R.layout.fragment_request_new_card),OnBackPressedFragment {

    companion object {
        fun newInstance() = RequestNewCardFragment()

        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(CardsFragmentDirections.actionCardsFragmentToRequestNewCardFragment())
    }

    override fun setUp() {
        gotoStepInfo()
        binding.apply {

        }

    }

    override fun listener() {
        with(binding) {
            btnConfirm.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    if (!isMonoClickable()) return@setOnClickListener
                    monoLastTimeClick()

                    if (getCurrentStep() == 1) {
                        gotoStepDetail()
                    } else {
                        Log.d("LOGD", "listener: call api new card")
                        activity?.finish()
                    }
                }
            }
        }

    }

    private fun getCurrentStep(): Int = if (binding.layoutEnterAddress.root.isVisible) {
        1
    } else {
        2
    }

    private fun gotoStepInfo() {
        binding.apply {
            isStepInfo = true
        }
    }

    private fun gotoStepDetail() {
        binding.apply {
            isStepInfo = false
        }
    }

    override fun onBackPressed(): Boolean {
        return if(getCurrentStep() == 1){
            false
        }else{
            gotoStepInfo()
            true
        }
    }

}
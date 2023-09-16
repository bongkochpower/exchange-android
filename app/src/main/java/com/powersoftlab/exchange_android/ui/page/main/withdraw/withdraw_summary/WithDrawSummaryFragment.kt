package com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_summary

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.navigator.AppNavigator
import com.powersoftlab.exchange_android.databinding.FragmentWithdrawSummaryBinding
import com.powersoftlab.exchange_android.databinding.FragmentWithdrawTypeBinding
import com.powersoftlab.exchange_android.ui.dialog.popup.AlertSuccessDialog
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.main.withdraw.auth_with_bio.AuthWithBioFragmentDirections
import com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_input_money.WithDrawInputMoneyFragmentDirections
import com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_type.WithDrawTypeFragmentDirections

class WithDrawSummaryFragment : BaseFragment<FragmentWithdrawSummaryBinding>(R.layout.fragment_withdraw_summary){

    companion object{
        fun newInstance() = WithDrawSummaryFragment()
        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(AuthWithBioFragmentDirections.actionAuthWithBioFragmentToWithDrawSummaryFragment())

    }

    override fun setUp() {

    }

    override fun listener() {
        with(binding){
            btnSavePicture.setOnClickListener {
                val msg = resources.getString(R.string.message_save_picture_success)
                val btn = resources.getString(R.string.button_back_to_main)
                showAlertSuccessDialog(title = msg, textButtonRight = btn) {
                    activity?.finish()
                }
            }
        }

    }

}
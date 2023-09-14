package com.powersoftlab.exchange_android.ui.page.main.withdraw.auth_with_bio

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.FragmentAuthWithBioBinding
import com.powersoftlab.exchange_android.databinding.FragmentWithdrawInputMoneyBinding
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_input_money.WithDrawInputMoneyFragment
import com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_input_money.WithDrawInputMoneyFragmentDirections
import com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_summary.WithDrawSummaryFragment
import com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_type.WithDrawTypeFragmentDirections

class AuthWithBioFragment : BaseFragment<FragmentAuthWithBioBinding>(R.layout.fragment_auth_with_bio),OnBackPressedFragment {

    companion object {
        fun newInstance() = AuthWithBioFragment()
        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(WithDrawInputMoneyFragmentDirections.actionWithDrawInputMoneyFragmentToAuthWithBioFragment())
    }

    override fun setUp() {

    }

    override fun listener() {
        binding.apply {
            btnConfirm.setOnClickListener {
                gotoSummary()
            }
        }
    }

    private fun gotoSummary() {
        WithDrawSummaryFragment.navigate(this@AuthWithBioFragment)
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}
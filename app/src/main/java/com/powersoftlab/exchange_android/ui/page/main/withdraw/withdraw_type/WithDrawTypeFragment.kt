package com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_type

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.FragmentCardsBinding
import com.powersoftlab.exchange_android.databinding.FragmentWithdrawTypeBinding
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.cards.CardsFragment
import com.powersoftlab.exchange_android.ui.page.main.cards.CardsFragmentDirections
import com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_input_money.WithDrawInputMoneyFragment

class WithDrawTypeFragment : BaseFragment<FragmentWithdrawTypeBinding>(R.layout.fragment_withdraw_type),OnBackPressedFragment {

    companion object {
        fun newInstance() = WithDrawTypeFragment()
    }

    override fun setUp() {
        with(binding) {

        }
    }

    override fun listener() {
        with(binding) {
            btnNextStep.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    if (!isMonoClickable()) return@setOnClickListener
                    monoLastTimeClick()

                    gotoInputMoney()

                }
            }

            btnScanQr.apply {
                setOnTouchAnimation()
                setOnClickListener {  }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    private fun gotoInputMoney() {
        WithDrawInputMoneyFragment.navigate(this@WithDrawTypeFragment)
    }

}
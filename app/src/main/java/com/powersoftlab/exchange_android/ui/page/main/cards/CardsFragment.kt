package com.powersoftlab.exchange_android.ui.page.main.cards

import android.util.Log
import android.view.View
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.FragmentCardsBinding
import com.powersoftlab.exchange_android.databinding.FragmentLoginBinding
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.login.register.register.RegisterFragment
import com.powersoftlab.exchange_android.ui.page.main.cards.request_new_card.RequestNewCardFragment

class CardsFragment : BaseFragment<FragmentCardsBinding>(R.layout.fragment_cards),OnBackPressedFragment {

    companion object {
        fun newInstance() = CardsFragment()
    }

    override fun setUp() {
        with(binding) {
        }

    }

    override fun listener() {
        with(binding) {
            btnReqNewCard.apply {
                setOnTouchAnimation()
                setOnClickListener { goToRequestNewCard() }
            }
        }
    }

    private fun goToRequestNewCard() {
        RequestNewCardFragment.navigate(this@CardsFragment)
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}
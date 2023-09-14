package com.powersoftlab.exchange_android.ui.page.main.cards

import android.util.Log
import android.view.View
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.FragmentCardsBinding
import com.powersoftlab.exchange_android.databinding.FragmentLoginBinding
import com.powersoftlab.exchange_android.databinding.ItemRvCardsBinding
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ext.toCardNumberFormat
import com.powersoftlab.exchange_android.model.response.CardsModel
import com.powersoftlab.exchange_android.ui.list.LoadingStyleEnum
import com.powersoftlab.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter
import com.powersoftlab.exchange_android.ui.list.viewholder.bind.CardsViewHolderHelper.initCards
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.login.register.register.RegisterFragment
import com.powersoftlab.exchange_android.ui.page.main.cards.request_new_card.RequestNewCardFragment

class CardsFragment : BaseFragment<FragmentCardsBinding>(R.layout.fragment_cards),OnBackPressedFragment {

    private val cardsAdapter by lazy {
        SimpleRecyclerViewAdapter<CardsModel, ItemRvCardsBinding>(
            layout = R.layout.item_rv_cards,
            loadingStyleEnum = LoadingStyleEnum.SK_TRANSACTION
        )
    }

    companion object {
        fun newInstance() = CardsFragment()
    }

    override fun setUp() {
        with(binding) {
            vpCards.adapter = cardsAdapter

        }
        val dummyCards = listOf<CardsModel>(
            CardsModel("1231231231231231".toCardNumberFormat(),"12/12/","dev 002"),
            CardsModel("3213231232123131".toCardNumberFormat(),"12/12","dev 001"),
            CardsModel("5123412324123112".toCardNumberFormat(),"12/12/12","dev 003")
        )
        cardsAdapter.submitList(true,dummyCards.toMutableList())
        cardsAdapter.initCards(requireContext())

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
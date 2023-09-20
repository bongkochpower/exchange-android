package com.powersoftlab.exchange_android.ui.page.main.cards

import androidx.core.view.isVisible
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.databinding.FragmentCardsBinding
import com.powersoftlab.exchange_android.databinding.ItemRvCardsBinding
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ext.toCardNumberFormat
import com.powersoftlab.exchange_android.model.response.CardsResponseModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.list.LoadingStyleEnum
import com.powersoftlab.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter
import com.powersoftlab.exchange_android.ui.list.viewholder.bind.CardsViewHolderHelper.initCards
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.cards.request_new_card.RequestNewCardFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class CardsFragment : BaseFragment<FragmentCardsBinding>(R.layout.fragment_cards),OnBackPressedFragment {

    private val cardsViewModel : CardsViewModel by stateViewModel()

    private val cardsAdapter by lazy {
        SimpleRecyclerViewAdapter<CardsResponseModel, ItemRvCardsBinding>(
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
        cardsViewModel.getCards()

        val dummyCards = listOf<CardsResponseModel>(
            CardsResponseModel("1231231231231233".toCardNumberFormat(),1,"12/12","classic","username 001"),
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

    override fun subscribe() {
        super.subscribe()

        cardsViewModel.getCardsResult().observe(viewLifecycleOwner){
            cardsAdapter.isLoading = false
            when (it) {
                is ResultWrapper.Loading -> {
                    cardsAdapter.isLoading = true
                }
                is ResultWrapper.Success -> {
                    it.response.data?.let { it1 ->
                        setCardData(
                            it1
                        )
                    }
                }
                is ResultWrapper.GenericError -> {
                    //AppAlert.alertGenericError(requireContext(), it.code, it.message).show(childFragmentManager)
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alertNetworkError(requireContext()).show(childFragmentManager)
                }

                else -> {
                    /*none*/
                }
            }
        }
    }

    private fun setCardData(list : List<CardsResponseModel>){
        binding.layoutCards.isVisible = list.isNotEmpty()
        cardsAdapter.updateList(list.toMutableList(),true)
    }

}
package com.powersoftlab.exchange_android.ui.list.viewholder.bind

import android.content.Context
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.ItemRvCardsBinding
import com.powersoftlab.exchange_android.ext.toCardNumberFormat
import com.powersoftlab.exchange_android.ext.toDashWhenNullOrEmpty
import com.powersoftlab.exchange_android.model.response.CardsResponseModel
import com.powersoftlab.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter


object CardsViewHolderHelper {

    fun SimpleRecyclerViewAdapter<CardsResponseModel, ItemRvCardsBinding>.initCards(
        context: Context
    ) {
        onBindView { binding, item, position ->
            with(binding) {
                item?.let {
                    val cardNo = it.cardNo.orEmpty().toCardNumberFormat()
                    val cardDate = it.validDate.toDashWhenNullOrEmpty()
                    val cardName = it.cardName
                    val cardUserName = it.yourName.toDashWhenNullOrEmpty()

                    tvCardNo.text = cardNo
                    tvCardDate.text = context.getString(R.string.hint_card_date,cardDate)
                    tvCardName.text = cardName
                    tvCardUsername.text = cardUserName

                }
            }
        }
    }

}
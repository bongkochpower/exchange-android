package com.powersoftlab.exchange_android.ui.list.viewholder.bind

import android.content.Context
import android.util.Log
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.constant.AppConstant
import com.powersoftlab.exchange_android.common.enum.TransactionStatusEnum
import com.powersoftlab.exchange_android.common.enum.TransactionTypeEnum
import com.powersoftlab.exchange_android.databinding.ItemRvBalanceBinding
import com.powersoftlab.exchange_android.databinding.ItemRvCardsBinding
import com.powersoftlab.exchange_android.databinding.ItemRvTransactionBinding
import com.powersoftlab.exchange_android.ext.convertDisplayDateToBuddhistYear
import com.powersoftlab.exchange_android.ext.convertUtcToIct
import com.powersoftlab.exchange_android.ext.loadImage
import com.powersoftlab.exchange_android.ext.reDateFormat
import com.powersoftlab.exchange_android.ext.toCompactDecimalFormat
import com.powersoftlab.exchange_android.ext.toDisplayFormat
import com.powersoftlab.exchange_android.model.response.CardsModel
import com.powersoftlab.exchange_android.model.response.TransactionsModel
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter


object CardsViewHolderHelper {

    fun SimpleRecyclerViewAdapter<CardsModel, ItemRvCardsBinding>.initCards(
        context: Context
    ) {
        onBindView { binding, item, position ->
            with(binding) {
                item?.let {
                    val cardNo = it.cardNo
                    val cardDate = it.validDate
                    val cardName = it.cardName

                    tvCardNo.text = cardNo
                    tvCardDate.text = context.getString(R.string.hint_card_date,12,30)
                    tvCardName.text = cardName

                }
            }
        }
    }

}
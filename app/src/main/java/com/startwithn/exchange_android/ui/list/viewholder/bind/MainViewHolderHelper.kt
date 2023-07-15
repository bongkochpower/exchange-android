package com.startwithn.exchange_android.ui.list.viewholder.bind

import android.content.Context
import androidx.core.content.ContextCompat
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.common.constant.AppConstant
import com.startwithn.exchange_android.databinding.ItemRvBalanceBinding
import com.startwithn.exchange_android.ext.toPointStringFormat
import com.startwithn.exchange_android.ext.toStringFormat
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter

object MainViewHolderHelper {

    fun SimpleRecyclerViewAdapter<UserModel.CustomerBalance, ItemRvBalanceBinding>.initBalance(
        context: Context
    ) {
        onBindView { binding, item, position ->
            with(binding) {
                item?.let {
                    val label = it.label
                    val amount = it.balance

                    tvTitle.text = label
                    tvMoney.text = amount?.toDouble()?.toStringFormat()
                }
            }
        }
    }


}
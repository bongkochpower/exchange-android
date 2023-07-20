package com.startwithn.exchange_android.ui.list.viewholder.bind

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.ContextCompat
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ItemRvExchangeCurrencyBinding
import com.startwithn.exchange_android.ext.isMonoClickable
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter

object ExchangeHolderHelper {

    fun SimpleRecyclerViewAdapter<UserModel.CustomerBalance, ItemRvExchangeCurrencyBinding>.initExchangeFrom(
        context: Context,
        callbackSelected: (UserModel.CustomerBalance) -> Unit
    ) {
        var selectedPosition: Int = 0
        onBindView { binding, item, position ->
            with(binding) {
                isFrom = true
                item?.let { item ->
                    val title = item.label
                    item.isSelected = selectedPosition == position
                    tvCurrentTextFrom.apply {
                        text = title
                        isChecked = item.isSelected
                    }

                    tvCurrentTextFrom.setOnClickListener { view ->
                        selectedPosition = position
                        callbackSelected.invoke(item)
                        notifyDataSetChanged()
                    }

                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun SimpleRecyclerViewAdapter<UserModel.CustomerBalance, ItemRvExchangeCurrencyBinding>.initExchangeTo(
        context: Context,
        callbackSelected: (UserModel.CustomerBalance) -> Unit
    ) {
        var selectedPosition: Int = 0
        onBindView { binding, item, position ->
            with(binding) {
                isFrom = false
                item?.let { item ->
                    val title = item.label

                    item.isSelected = selectedPosition == position
                    tvCurrentTextTo.apply {
                        text = title
                        isChecked = item.isSelected
                    }

                    tvCurrentTextTo.setOnClickListener { view ->
                        selectedPosition = position
                        callbackSelected.invoke(item)
                        notifyDataSetChanged()
                    }

                }
            }
        }
    }



}
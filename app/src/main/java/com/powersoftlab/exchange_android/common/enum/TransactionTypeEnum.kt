package com.powersoftlab.exchange_android.common.enum

import com.powersoftlab.exchange_android.R

enum class TransactionTypeEnum(val title: String, val iconRes: Int) {
    DEPOSIT("DEPOSIT", R.drawable.icon_topup),
    WITHDRAW("WITHDRAW",R.drawable.icon_topup),
    CHANGE("CHANGE",R.drawable.icon_exchange),
    DEPOSIT_BY_CHANGE("DEPOSIT_BY_CHANGE",R.drawable.icon_topup);

    companion object {
        fun fromName(name: String?): TransactionTypeEnum {
            if (name != null) {
                values().find { name.equals(it.name, true) }?.let {
                    return it
                }
            }
            return DEPOSIT
        }
    }
}
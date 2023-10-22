package com.powersoftlab.exchange_android.common.enum

import com.powersoftlab.exchange_android.R

enum class TransactionTypeEnum(val code: String,val title: String, val iconRes: Int) {
    DEPOSIT("DEPOSIT","Deposit", R.drawable.icon_topup),
    WITHDRAW("WITHDRAW","Withdraw",R.drawable.icon_withdraw),
    CHANGE("CHANGE","Change",R.drawable.icon_exchange),
    DEPOSIT_BY_CHANGE("DEPOSIT_BY_CHANGE","Deposit By Change",R.drawable.icon_topup),
    TRANSFER_OUT("TRANSFER_OUT","Transfer Out",R.drawable.icon_transfer_out),
    TRANSFER_IN("TRANSFER_IN","Transfer In",R.drawable.icon_transfer_out)
    ;


    companion object {
        fun fromName(code: String?): TransactionTypeEnum {
            if (code != null) {
                values().find { code.equals(it.code, true) }?.let {
                    return it
                }
            }
            return DEPOSIT
        }
    }
}
package com.powersoftlab.exchange_android.common.enum

import com.powersoftlab.exchange_android.R

enum class TransactionStatusEnum(val title: String,val drawableRes: Int) {
    DONE("Done", R.drawable.bg_transaction_success),
    PENDING("Pending",R.drawable.bg_transaction_pending),
    FAILED("Failed",R.drawable.bg_transaction_fail);

    companion object {
        fun fromName(name: String?): TransactionStatusEnum {
            if (name != null) {
                values().find { name.equals(it.name, true) }?.let {
                    return it
                }
            }
            return FAILED
        }
    }
}
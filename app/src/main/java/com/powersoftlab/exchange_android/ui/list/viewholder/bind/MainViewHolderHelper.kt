package com.powersoftlab.exchange_android.ui.list.viewholder.bind

import android.content.Context
import com.powersoftlab.exchange_android.common.constant.AppConstant
import com.powersoftlab.exchange_android.common.enum.TransactionStatusEnum
import com.powersoftlab.exchange_android.common.enum.TransactionTypeEnum
import com.powersoftlab.exchange_android.databinding.ItemRvBalanceBinding
import com.powersoftlab.exchange_android.databinding.ItemRvTransactionBinding
import com.powersoftlab.exchange_android.ext.convertUtcToIct
import com.powersoftlab.exchange_android.ext.loadImage
import com.powersoftlab.exchange_android.ext.reDateFormat
import com.powersoftlab.exchange_android.ext.toCompactDecimalFormat
import com.powersoftlab.exchange_android.ext.toDisplayFormat
import com.powersoftlab.exchange_android.model.response.TransactionsModel
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter


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
                    tvMoney.text = amount?.toCompactDecimalFormat()

                }
            }
        }
    }
    


    fun SimpleRecyclerViewAdapter<TransactionsModel, ItemRvTransactionBinding>.initTransactions(
        context: Context
    ) {
        onBindView { binding, item, position ->
            with(binding) {
                item?.let {
                    //วันที่ 02/06/23 เวลา 18:00 น.
                    val dateTime = it.createdAt.convertUtcToIct()

                    val date = dateTime.toDisplayFormat()
                    val time = dateTime.reDateFormat(AppConstant.FORMAT_SERVICE_DATE_TIME, AppConstant.FORMAT_UI_TIME)
                    val status = TransactionStatusEnum.fromName(it.status)



                    val transactionEnum = TransactionTypeEnum.fromName(it.type)
                    tvTitle.text = transactionEnum.title
                    tvTimeAgo.text = "Date $date Time $time"
                    tvTransactionStatus.apply {
                        text = status.title
                        setBackgroundResource(status.drawableRes)
                    }
                    tvMoneyTrans.text = it.message
                    imgTransaction.loadImage(transactionEnum.iconRes)

                }
            }
        }
    }


}
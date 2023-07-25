package com.startwithn.exchange_android.ui.list.viewholder.bind

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.startwithn.exchange_android.common.constant.AppConstant
import com.startwithn.exchange_android.common.enum.TransactionStatusEnum
import com.startwithn.exchange_android.common.enum.TransactionTypeEnum
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.databinding.ItemRvBalanceBinding
import com.startwithn.exchange_android.databinding.ItemRvTransactionBinding
import com.startwithn.exchange_android.ext.convertDisplayDateToBuddhistYear
import com.startwithn.exchange_android.ext.convertUtcToIct
import com.startwithn.exchange_android.ext.loadImage
import com.startwithn.exchange_android.ext.reDateFormat
import com.startwithn.exchange_android.ext.toCompactDecimalFormat
import com.startwithn.exchange_android.ext.toDisplayFormat
import com.startwithn.exchange_android.ext.toStringFormat
import com.startwithn.exchange_android.model.response.TransactionsModel
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone


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

                    val date = dateTime.toDisplayFormat().convertDisplayDateToBuddhistYear()
                    val time = dateTime.reDateFormat(AppConstant.FORMAT_SERVICE_DATE_TIME, AppConstant.FORMAT_UI_TIME)
                    val status = TransactionStatusEnum.fromName(it.status)



                    tvTitle.text = it.typeText
                    tvTimeAgo.text = "วันที่ $date เวลา $time น."
                    tvTransactionStatus.apply {
                        text = status.title
                        setBackgroundResource(status.drawableRes)
                    }
                    tvMoneyTrans.text = it.message
                    val iconRes = TransactionTypeEnum.fromName(it.type)
                    imgTransaction.loadImage(iconRes.iconRes)

                }
            }
        }
    }


}
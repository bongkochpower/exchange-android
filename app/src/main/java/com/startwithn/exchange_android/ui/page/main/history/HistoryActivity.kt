package com.startwithn.exchange_android.ui.page.main.history

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.common.alert.AppAlert
import com.startwithn.exchange_android.databinding.ActivityHistoryBinding
import com.startwithn.exchange_android.databinding.ActivityTopupBinding
import com.startwithn.exchange_android.databinding.ItemRvTransactionBinding
import com.startwithn.exchange_android.ext.fadeIn
import com.startwithn.exchange_android.ext.getDatePickerDialog
import com.startwithn.exchange_android.ext.gone
import com.startwithn.exchange_android.ext.setItemPadding
import com.startwithn.exchange_android.ext.setOnLoadMoreListener
import com.startwithn.exchange_android.ext.setOnTouchAnimation
import com.startwithn.exchange_android.ext.slideUp
import com.startwithn.exchange_android.ext.toServiceFormat
import com.startwithn.exchange_android.model.response.TransactionsModel
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter
import com.startwithn.exchange_android.ui.list.itemdecoration.EqualSpacingItemDecoration
import com.startwithn.exchange_android.ui.list.viewholder.bind.MainViewHolderHelper.initTransactions
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import com.startwithn.exchange_android.ui.page.main.MainViewModel
import com.startwithn.exchange_android.ui.page.main.topup.TopUpActivity
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class HistoryActivity : BaseActivity<ActivityHistoryBinding>(R.layout.activity_history) {

    private val historyViewModel: HistoryViewModel by stateViewModel()

    private val historyAdapter by lazy {
        SimpleRecyclerViewAdapter<TransactionsModel, ItemRvTransactionBinding>(
            layout = R.layout.item_rv_transaction,
            isRunAnimation = true
        )
    }

    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, HistoryActivity::class.java).apply {

            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun setUp() {
        binding.apply {
            showTransactionListView(false)

            rvHistory.apply {
                adapter = historyAdapter
                setOnLoadMoreListener({ historyAdapter.isLoading }, {
                    //contactUsViewModel.getContact(contactUsViewModel.page)
                })
            }
            historyAdapter.initTransactions(this@HistoryActivity)
        }
    }

    override fun listener() {
        with(binding) {
            toolbar.setOnBackListener {
                onBackPressed()
            }

            imgDateFrom.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    showDatePickerDialog { date ->
                        edtStartDate.setText(date)
                    }
                }
            }

            imgDateTo.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    showDatePickerDialog { date ->
                        edtEndDate.setText(date)
                    }
                }
            }

            btnSlideConfirm.setOnClickListener {
                if(isValidate()){
                    val dateFrom = edtStartDate.text.toString()
                    val dateTo = edtEndDate.text.toString()

                    showTransactionListView(true)
                    getHistory(dateFrom,dateTo)
                }
            }
        }
    }

    override fun subscribe() {
        super.subscribe()

        historyViewModel.historyRequestLiveData.observe(this) {
            historyAdapter.isLoading = false
            when (it) {
                is ResultWrapper.Loading -> {
                    historyAdapter.isLoading = true
                }
                is ResultWrapper.GenericError -> {
                    AppAlert.alertGenericError(this,it.code, it.message).show(supportFragmentManager)
                }
                is ResultWrapper.NetworkError -> {
                    AppAlert.alertNetworkError(this).show(supportFragmentManager)
                }
                else -> {
                    /*none*/
                }
            }
        }
        historyViewModel.historyResultLiveData.observe(this) {
            it?.let {
                historyAdapter.submitList(true, it)
                binding.isEmpty = historyAdapter.itemCount == 0
            }
        }
    }

    private fun showDatePickerDialog(cb: (String) -> Unit) {
        val datePickerDialog = this@HistoryActivity.getDatePickerDialog(
            cb = { date: String ->
                cb.invoke(date)
            },
            isMaxToday = true
        )
        datePickerDialog.dismiss()
        datePickerDialog.show()
    }

    private fun showTransactionListView(isShow: Boolean) {
        with(binding) {
            layoutHistoryList.isVisible = isShow
            tvTransactionList.isVisible = isShow
            tvSelectedDate.isVisible = isShow

            if(isShow){
                val dateFrom = edtStartDate.text.toString()
                val dateTo = edtEndDate.text.toString()
                val textDateSelected = resources.getString(R.string.title_history_date_select).replace("#start",dateFrom).replace("#end",dateTo)
                tvSelectedDate.text = textDateSelected
            }
        }
    }

    private fun isValidate() : Boolean{
        var isValidate = false
        with(binding) {
            val dateFrom = edtStartDate.text.toString()
            val dateTo = edtEndDate.text.toString()

            when {
                dateFrom.isEmpty() && dateTo.isEmpty() -> {
                    isValidate = false
                }

                else -> isValidate = true
            }

        }
        return isValidate
    }

    private fun getHistory(dateFrom: String, dateTo: String) {
        historyViewModel.getHistory(dateForm = dateFrom.toServiceFormat(), dateTo = dateTo.toServiceFormat())
    }

}
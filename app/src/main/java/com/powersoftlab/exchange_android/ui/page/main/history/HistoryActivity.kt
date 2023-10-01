package com.powersoftlab.exchange_android.ui.page.main.history

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.common.constant.AppConstant.FORMAT_UI_DATE
import com.powersoftlab.exchange_android.databinding.ActivityHistoryBinding
import com.powersoftlab.exchange_android.databinding.ItemRvTransactionBinding
import com.powersoftlab.exchange_android.ext.getCalendar
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setOnLoadMoreListener
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ext.setTextSlideButtonEnable
import com.powersoftlab.exchange_android.ext.toCalendar
import com.powersoftlab.exchange_android.ext.toDate
import com.powersoftlab.exchange_android.ext.toServiceFormat
import com.powersoftlab.exchange_android.ext.toString
import com.powersoftlab.exchange_android.model.response.TransactionsModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.list.LoadingStyleEnum
import com.powersoftlab.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter
import com.powersoftlab.exchange_android.ui.list.viewholder.bind.MainViewHolderHelper.initTransactions
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import java.util.Calendar


class HistoryActivity : BaseActivity<ActivityHistoryBinding>(R.layout.activity_history) {

    private val historyViewModel: HistoryViewModel by stateViewModel()

    private val historyAdapter by lazy {
        SimpleRecyclerViewAdapter<TransactionsModel, ItemRvTransactionBinding>(
            layout = R.layout.item_rv_transaction,
            isRunAnimation = true,
            loadingStyleEnum = LoadingStyleEnum.SK_TRANSACTION
        )
    }

    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, HistoryActivity::class.java).apply {}
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

            historyAdapter.submitList(true, mutableListOf())
            historyAdapter.initTransactions(this@HistoryActivity)

        }
    }

    override fun listener() {
        with(binding) {
            toolbar.setOnBackListener {
                onClose()
            }

            imgDateFrom.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    showDatePickerDialog({ date ->
                        edtStartDate.setText(date)

                        if (edtStartDate.text.isNotEmpty() && edtEndDate.text.isNotEmpty()) {
                            slideToConfirm.setTextSlideButtonEnable(true, R.string.button_slide_to_confirm)
                        }
                    }, edtStartDate.text.toString())
                }
            }

            imgDateTo.apply {
                setOnTouchAnimation()
                setOnClickListener {

                    if(edtStartDate.text.toString().isEmpty()){
                        AppAlert.alert(this@HistoryActivity,getString(R.string.validate_select_start_date)).show(supportFragmentManager)
                    }else{
                        showDatePickerDialog({ date ->
                            edtEndDate.setText(date)

                            if (edtStartDate.text.isNotEmpty() && edtEndDate.text.isNotEmpty()) {
                                slideToConfirm.setTextSlideButtonEnable(true, R.string.button_slide_to_confirm)
                            }
                        }, edtEndDate.text.toString(), false)
                    }
                }
            }

            swipeRefresh.setOnRefreshListener {
                getHistory()
            }


            slideToConfirm.setTextSlideButtonEnable(false, R.string.button_slide_to_confirm)
            slideToConfirm.setOnClickListener {
                if (!isMonoClickable()) return@setOnClickListener
                monoLastTimeClick()

                slideToConfirm.setEnable(false)
                slideToConfirm.setBackgroundRes(R.drawable.bg_slide_confirm_done)

                getHistory()
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
                    binding.swipeRefresh.isRefreshing = historyAdapter.isLoading
                }

                is ResultWrapper.GenericError -> {
                    AppAlert.alertGenericError(this, it.code, it.message).show(supportFragmentManager)
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alertNetworkError(this).show(supportFragmentManager)
                }

                is ResultWrapper.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                }

                else -> {
                    /*none*/
                }
            }
        }
        historyViewModel.historyResultLiveData.observe(this) {
            it?.let {
                showTransactionListView(true)
                historyAdapter.updateList(it, true)
                binding.isEmpty = historyAdapter.itemCount == 0
            }
        }
    }

    private fun showTransactionListView(isShow: Boolean) {
        with(binding) {
            layoutHistoryList.isVisible = isShow
            tvTransactionList.isVisible = isShow
            tvSelectedDate.isVisible = isShow

            if (isShow) {
                val dateFrom = edtStartDate.text.toString()
                val dateTo = edtEndDate.text.toString()
                val textDateSelected = resources.getString(R.string.title_history_date_select, dateFrom, dateTo)
                val isSelected = (dateFrom.isNotEmpty() && dateTo.isNotEmpty())
                tvSelectedDate.isVisible = isSelected
                tvSelectedDate.text = textDateSelected
            }
        }
    }

    private fun isValidate(): Boolean {
        var isValidate = false
        with(binding) {
            val dateFrom = edtStartDate.text.toString()
            val dateTo = edtEndDate.text.toString()
            when {
                dateFrom.isEmpty() && dateTo.isEmpty() -> {}
                else -> isValidate = true
            }

        }
        return isValidate
    }

    private fun onClose() {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    private fun getHistory() {
        if (isValidate()) {
            val dateFrom = binding.edtStartDate.text.toString()
            val dateTo = binding.edtEndDate.text.toString()

            val dateFromTimeStamp = dateFrom.toDate(FORMAT_UI_DATE).toCalendar().timeInMillis
            val dateToTimeStamp = dateTo.toDate(FORMAT_UI_DATE).toCalendar().timeInMillis

            if(dateFromTimeStamp > dateToTimeStamp){
                AppAlert.alert(this@HistoryActivity,getString(R.string.validate_select_history_time_invalid)).show(supportFragmentManager)
                binding.slideToConfirm.setTextSlideButtonEnable(true,R.string.button_slide_to_confirm)
                return
            }

            showTransactionListView(true)
            getHistory(dateFrom, dateTo)
        } else {
            historyViewModel.getLastHistory()
        }
    }

    private fun getHistory(dateFrom: String, dateTo: String) {
        historyViewModel.getHistory(dateForm = dateFrom.toServiceFormat(), dateTo = dateTo.toServiceFormat())
    }

    private fun showDatePickerDialog(cb: (String) -> Unit, dateSelect: String? = null, isDateFrom: Boolean = true) {
        val calendar = getCalendar()
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = calendar.get(Calendar.MONTH)
        if (dateSelect?.isNotEmpty() == true) {
            day = dateSelect.split("/")[0].toInt()
            month = dateSelect.split("/")[1].toInt() - 1
        }


        val datePickerDialog = this@HistoryActivity.getDatePickerHistoryDialog(
            cb = { date: String ->
                cb.invoke(date)
            },
            isMaxToday = true,
            day = day,
            month = month,
            isDateFromSelect = isDateFrom
        )

        datePickerDialog.dismiss()
        datePickerDialog.show()
    }

    private fun getDatePickerHistoryDialog(
        cb: (date: String) -> Unit,
        calendar: Calendar = getCalendar(),
        isMaxToday: Boolean = false,
        isDateFromSelect: Boolean = true,
        year: Int = calendar.get(Calendar.YEAR),
        month: Int = calendar.get(Calendar.MONTH),
        day: Int = calendar.get(Calendar.DAY_OF_MONTH),
    ): DatePickerDialog {
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = monthOfYear
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                cb.invoke(calendar.toString(FORMAT_UI_DATE))
            },
            year,
            month,
            day
        )

        if (isMaxToday) {
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        }
        if (!isDateFromSelect) {
            val fromDate = binding.edtStartDate.text.toString().toDate(FORMAT_UI_DATE).toCalendar().timeInMillis
            datePickerDialog.datePicker.minDate = fromDate
        }
        return datePickerDialog
    }

}
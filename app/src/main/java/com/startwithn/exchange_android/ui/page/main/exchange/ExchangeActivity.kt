package com.startwithn.exchange_android.ui.page.main.exchange

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.common.alert.AppAlert
import com.startwithn.exchange_android.databinding.ActivityExchangeBinding
import com.startwithn.exchange_android.databinding.ItemRvExchangeCurrencyBinding
import com.startwithn.exchange_android.databinding.ItemRvTransactionBinding
import com.startwithn.exchange_android.ext.hideKeyboard
import com.startwithn.exchange_android.ext.setOnLoadMoreListener
import com.startwithn.exchange_android.ext.setOnTouchAnimation
import com.startwithn.exchange_android.ext.toStringFormat
import com.startwithn.exchange_android.model.response.TransactionsModel
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.ui.list.LoadingStyleEnum
import com.startwithn.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter
import com.startwithn.exchange_android.ui.list.viewholder.bind.ExchangeHolderHelper.initExchangeFrom
import com.startwithn.exchange_android.ui.list.viewholder.bind.ExchangeHolderHelper.initExchangeTo
import com.startwithn.exchange_android.ui.list.viewholder.bind.MainViewHolderHelper.initTransactions
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import com.startwithn.exchange_android.ui.page.main.topup.TopUpActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import java.util.Timer
import kotlin.concurrent.schedule

class ExchangeActivity : BaseActivity<ActivityExchangeBinding>(R.layout.activity_exchange) {

    private val exchangeViewModel: ExchangeViewModel by stateViewModel()
    private val exchangeFromAdapter by lazy {
        SimpleRecyclerViewAdapter<UserModel.CustomerBalance, ItemRvExchangeCurrencyBinding>(
            layout = R.layout.item_rv_exchange_currency,
            isRunAnimation = true,
        )
    }

    private val exchangeToAdapter by lazy {
        SimpleRecyclerViewAdapter<UserModel.CustomerBalance, ItemRvExchangeCurrencyBinding>(
            layout = R.layout.item_rv_exchange_currency,
            isRunAnimation = true,
        )
    }

    companion object {

        private const val DELAY = 1000L

        fun open(activity: Activity) {
            val intent = Intent(activity, ExchangeActivity::class.java).apply {

            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun onResume() {
        super.onResume()
        getCurrencyExchangeFrom()
    }

    override fun setUp() {
        hideKeyboard()
        with(binding) {
            activity = this@ExchangeActivity
            isStep2 = false
            rvExchangeFrom.apply {
                adapter = exchangeFromAdapter
            }
            rvExchangeTo.apply {
                adapter = exchangeToAdapter
            }

            exchangeFromAdapter.initExchangeFrom(this@ExchangeActivity) {
                updateCurrencyFormSelected(it)
            }
            exchangeToAdapter.initExchangeTo(this@ExchangeActivity) {
                updateCurrencyFormSelected(it, false)
            }
        }
    }

    override fun listener() {
        with(binding) {
            toolbar.setOnBackListener {
                if (getCurrentStep() == 1) {
                    onBackPressed()
                } else {
                    gotoStep1()
                }
            }

            edtCurrencyFrom.addTextChangedListener(object : TextWatcher {
                var timer = Timer()
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    timer.cancel()
                }

                override fun afterTextChanged(it: Editable?) {
                    if(it.isNullOrEmpty()){
                        tvResultCurrency.text = "0.00"
                    }else{
                        timer.cancel()
                        timer = Timer()
                        timer.schedule(DELAY) {
                            if(it.isNotEmpty()){
                                val amount = it.toString().trim().replace(",","").toDoubleOrNull() ?: 0.0
                                exchangeViewModel.amount = amount
                                lifecycleScope.launch {
                                    exchangeViewModel.exchangeCalculate()
                                }
                            }
                        }
                    }

                }
            })

            btnContinue.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    gotoStep2()
                }
            }

            btnExchange.setOnClickListener {
                if(isValidate()){
                    exchange()
                }
            }
        }
    }


    override fun subscribe() {
        super.subscribe()

        exchangeViewModel.exchangeCalculateLiveData.observe(this) {
            when (it) {
                is ResultWrapper.Loading -> {
                    //showLoading()
                }
                is ResultWrapper.GenericError -> {
                    AppAlert.alertGenericError(this,it.code, it.message).show(supportFragmentManager)
//                    hideLoading()
                }
                is ResultWrapper.NetworkError -> {
                    AppAlert.alertNetworkError(this).show(supportFragmentManager)
//                    hideLoading()
                }
                is ResultWrapper.Success -> {
//                    hideLoading()
                    binding.tvResultCurrency.text = it.response.value?.toStringFormat()
                }
                else -> {
//                    hideLoading()
                }
            }
        }

        exchangeViewModel.exchangeLiveData.observe(this) {
            when (it) {
                is ResultWrapper.Loading -> {
                    showLoading()
                }
                is ResultWrapper.GenericError -> {
                    AppAlert.alertGenericError(this,it.code, it.message).show(supportFragmentManager)
                    hideLoading()
                }
                is ResultWrapper.NetworkError -> {
                    AppAlert.alertNetworkError(this).show(supportFragmentManager)
                    hideLoading()
                }
                is ResultWrapper.Success -> {
                    exchangeSuccess()
                    hideLoading()
                }
                else -> hideLoading()
            }
        }

    }

    private fun getCurrencyExchangeFrom() {
        val list = exchangeViewModel.getCurrencyForm()
        list.first().isSelected = true
        exchangeFromAdapter.submitList(true, list)

        getCurrencyExchangeTo(list.first())
    }

    private fun getCurrencyExchangeTo(selectedForm: UserModel.CustomerBalance) {
        val list = exchangeViewModel.getCurrencyForm()
        val listFiltered = list.filter { it.label != selectedForm.label }
        exchangeToAdapter.submitList(true, listFiltered.toMutableList())
    }

    private fun updateCurrencyFormSelected(currencyModel: UserModel.CustomerBalance, isCurrencyFrom: Boolean = true) {
        val txtCurrency = "${resources.getString(R.string.hint_exchange_your_balance_from)} ${currencyModel.balance?.toStringFormat()} ${currencyModel.label}"
        if (isCurrencyFrom) {
            if (!exchangeFromAdapter.isLoading) {
                binding.tvCurrencyForm.text = txtCurrency
                lifecycleScope.launch {
                    getCurrencyExchangeTo(currencyModel)
                }
            }
        } else {
            if (!exchangeToAdapter.isLoading) {
                binding.tvCurrencyTo.text = txtCurrency
            }
        }

    }

    private fun getCurrentStep(): Int = if (binding.btnContinue.isVisible) {
        1
    } else {
        2
    }

    private fun gotoStep1() {
        with(binding) {
            toolbar.setIcon(R.drawable.icon_close)
            isStep2 = false
        }
    }

    private fun gotoStep2() {
        with(binding) {
            toolbar.setIcon(R.drawable.icon_back)
            edtCurrencyFrom.text?.clear()
            edtCurrencyFrom.requestFocus()
            tvResultCurrency.text = "0.00"
            isStep2 = true
        }
    }

    private fun isValidate(): Boolean {
        var isValidate = false
        with(binding) {
            val inputMoney = edtCurrencyFrom.text.toString()
            when {
                inputMoney.isEmpty() || inputMoney == "0" -> {
                    isExchangeInvalid = true
                }
                else -> isValidate = true
            }
        }
        return isValidate
    }

    private fun exchange() {
        exchangeViewModel.exchange()
    }

    private fun exchangeSuccess(){
        val msg = resources.getString(R.string.message_exchange_success)
        val btn = resources.getString(R.string.button_back_to_main)
        showAlertSuccessDialog(msg, btn) {
            finish()
        }
    }


}
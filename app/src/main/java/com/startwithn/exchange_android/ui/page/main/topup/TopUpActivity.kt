package com.startwithn.exchange_android.ui.page.main.topup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.TextWatcher
import android.util.Log
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.crashlytics.internal.common.Utils
import com.ncorti.slidetoact.SlideToActView
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.common.alert.AppAlert
import com.startwithn.exchange_android.databinding.ActivityTopupBinding
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import com.startwithn.exchange_android.ui.widget.NumberPad
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class TopUpActivity : BaseActivity<ActivityTopupBinding>(R.layout.activity_topup) {

    private val viewModel : TopUpViewModel by stateViewModel()

    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, TopUpActivity::class.java).apply {

            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun setUp() {
        with(binding) {
            isTopupInvalid = false
        }
    }

    override fun listener() {
        with(binding) {

            toolbar.setOnBackListener {
                onBackPressed()
            }

            edtMoneyAmount.doAfterTextChanged {
                if (isTopupInvalid == true) {
                    isTopupInvalid = false
                }
            }
            npTopup.setOnNumberClickListener { type, number ->
                when (type) {
                    NumberPad.NumberPadEnum.NUMBER -> {
                        edtMoneyAmount.append(number)
                    }

                    NumberPad.NumberPadEnum.DELETE -> {
                        deleteAmount()
                    }

                    NumberPad.NumberPadEnum.DOT -> {
                        edtMoneyAmount.append(".")
                    }
                }
            }


            slideConfirm.setOnClickListener {
                if (isValidate()) {
                    val amount = edtMoneyAmount.text.toString().replace(",", "").toDoubleOrNull() ?: 0.0
                    viewModel.topUp(amount = amount)
                }
            }
        }
    }

    override fun subscribe() {
        super.subscribe()

        viewModel.topUpLiveData.observe(this){
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
                    hideLoading()
                    topUpSuccess()
                }
                else -> {
                    /*none*/
                }
            }
        }
    }

    private fun isValidate(): Boolean {
        var isValidate = false
        with(binding) {
            val inputMoney = edtMoneyAmount.text.toString()
            when {
                inputMoney.isEmpty() || inputMoney == "0" -> {
                    isTopupInvalid = true
                }

                else -> isValidate = true
            }
        }
        return isValidate
    }

    private fun topUpSuccess() {
        val title = resources.getString(R.string.message_topup_success)
        val btn = resources.getString(R.string.button_back_to_main)
        showAlertSuccessDialog(title, btn) {
            finish()
        }
    }

    private fun deleteAmount() {
        with(binding) {
            edtMoneyAmount.text?.let { editable ->
                if (editable.isNotEmpty()) {
                    editable.delete(editable.length - 1, editable.length)
                    edtMoneyAmount.text = editable
                    edtMoneyAmount.setSelection(editable.length)
                }
            }
        }
    }


}
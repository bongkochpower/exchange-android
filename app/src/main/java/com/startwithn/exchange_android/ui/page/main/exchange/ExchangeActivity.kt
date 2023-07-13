package com.startwithn.exchange_android.ui.page.main.exchange

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ActivityExchangeBinding
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import com.startwithn.exchange_android.ui.page.main.topup.TopUpActivity

class ExchangeActivity : BaseActivity<ActivityExchangeBinding>(R.layout.activity_exchange) {
    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, ExchangeActivity::class.java).apply {

            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun setUp() {
        with(binding){

        }
    }

    override fun listener() {
        with(binding){
            toolbar.setOnBackListener {
                onBackPressed()
            }
        }
    }
}
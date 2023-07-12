package com.startwithn.exchange_android.ui.page.main.history

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ActivityHistoryBinding
import com.startwithn.exchange_android.databinding.ActivityTopupBinding
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import com.startwithn.exchange_android.ui.page.main.topup.TopUpActivity

class HistoryActivity : BaseActivity<ActivityHistoryBinding>(R.layout.activity_history) {
    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, HistoryActivity::class.java).apply {

            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun setUp() {

    }

    override fun listener() {
        with(binding) {
            toolbar.setOnBackListener {
                onBackPressed()
            }
        }
    }

}
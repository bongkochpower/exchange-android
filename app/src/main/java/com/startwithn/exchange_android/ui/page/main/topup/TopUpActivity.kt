package com.startwithn.exchange_android.ui.page.main.topup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html.FROM_HTML_MODE_LEGACY
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ActivityTopupBinding
import com.startwithn.exchange_android.ui.page.base.BaseActivity

class TopUpActivity : BaseActivity<ActivityTopupBinding>(R.layout.activity_topup) {

    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, TopUpActivity::class.java).apply {

            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun setUp() {
        binding.apply {
            tvTopupAmount.text = HtmlCompat.fromHtml(resources.getString(R.string.title_topup_amount),HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    override fun listener() {
        with(binding) {
            toolbar.setOnBackListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }


}
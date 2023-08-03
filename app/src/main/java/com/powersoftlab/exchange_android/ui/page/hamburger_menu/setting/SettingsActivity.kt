package com.powersoftlab.exchange_android.ui.page.hamburger_menu.setting

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.ActivitySettingsBinding
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity

class SettingsActivity : BaseActivity<ActivitySettingsBinding>(R.layout.activity_settings) {
    companion object {
        fun open(activity: Activity) =
            ContextCompat.startActivity(activity, Intent(activity, SettingsActivity::class.java), null)
    }

    override fun setUp() {
        with(binding){

        }
    }

    override fun listener() {
        binding.apply {
            appBarLayout.setOnBackListener {
                onBackPressed()
            }
        }
    }
}
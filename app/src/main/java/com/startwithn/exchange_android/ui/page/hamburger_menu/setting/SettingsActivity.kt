package com.startwithn.exchange_android.ui.page.hamburger_menu.setting

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.bind
import androidx.databinding.DataBindingUtil.setContentView
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ActivitySettingsBinding
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import com.startwithn.exchange_android.ui.page.hamburger_menu.help.HelpActivity

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
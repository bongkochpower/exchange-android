package com.startwithn.exchange_android.ui.page.hamburger_menu.help

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ActivityHelpBinding
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import com.startwithn.exchange_android.ui.page.login.LoginActivity

class HelpActivity : BaseActivity<ActivityHelpBinding>(R.layout.activity_help) {
    companion object {
        fun open(activity: Activity) =
            ContextCompat.startActivity(activity, Intent(activity, HelpActivity::class.java), null)
    }

    override fun setUp() {

    }

    override fun listener() {
        with(binding){
            appBarLayout.setOnBackListener {
                onBackPressed()
            }
        }

    }
}
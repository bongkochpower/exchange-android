package com.powersoftlab.exchange_android.ui.page.hamburger_menu.help

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.ActivityHelpBinding
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity

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
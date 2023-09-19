package com.powersoftlab.exchange_android.ui.page.intro.auth_pin

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.ActivityAuthenticationBinding
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity

class AuthPinActivity : BaseActivity<ActivityAuthenticationBinding>(R.layout.activity_authentication) {

    companion object {
        fun open(activity: Activity) =
            ContextCompat.startActivity(activity, Intent(activity, AuthPinActivity::class.java), null)
    }

    override fun setUp() {
        with(binding){
        }
    }

    override fun listener() {
    }

}
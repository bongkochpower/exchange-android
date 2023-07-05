package com.startwithn.exchange_android.ui.page.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ActivityLoginBinding
import com.startwithn.exchange_android.ext.getCurrentFragment
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import com.startwithn.exchange_android.ui.page.base.OnBackPressedFragment
import com.startwithn.exchange_android.ui.page.main.activity.MainActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    companion object {
        fun open(activity: Activity) =
            ContextCompat.startActivity(activity, Intent(activity, LoginActivity::class.java), null)
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is OnBackPressedFragment) {
            val isOverride: Boolean = currentFragment.onBackPressed()
            if (isOverride) {
                return
            }
        }
        super.onBackPressed()
    }
    override fun setUp() {

    }

    private fun getCurrentFragment(): Fragment =
        supportFragmentManager.getCurrentFragment(R.id.fragment_container_view)

    fun goToRegister(v: View) {
        //RegisterFragment.navigate(getCurrentFragment())
    }

    fun goToForgotPassword(v: View) {
        //ForgotPasswordFragment.navigate(getCurrentFragment())
    }
}
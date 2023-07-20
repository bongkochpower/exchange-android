package com.startwithn.exchange_android.ui.page.login

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ActivityLoginBinding
import com.startwithn.exchange_android.ext.getCurrentFragment
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import com.startwithn.exchange_android.ui.page.base.OnBackPressedFragment
import com.startwithn.exchange_android.ui.page.login.forgot.verify_phone_number.ForgotPasswordFragment
import com.startwithn.exchange_android.ui.page.login.register.register.RegisterFragment
import com.startwithn.exchange_android.ui.page.login.register.TermRegisterFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val loginViewModel: LoginViewModel by viewModel()

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
        with(binding){

        }
    }

    override fun listener() {
        with(binding){
            appBarLayout.setOnBackListener { onBackPressed() }
        }

    }

    override fun subscribe() {
        super.subscribe()

        loginViewModel.iconLeftMenu.observe(this){ iconRes ->
            binding.appBarLayout.setIcon(iconRes)
        }
    }

    private fun getCurrentFragment(): Fragment =
        supportFragmentManager.getCurrentFragment(R.id.fragment_container_view)

    fun goToRegister(v: View) {
        RegisterFragment.navigate(getCurrentFragment())
    }

    fun goToLogin(v : View){
        onBackPressed()
    }

    fun goToRegisterTerm(v : View){
        TermRegisterFragment.navigate(getCurrentFragment())
    }

    fun goToForgotPassword(v: View) {
        ForgotPasswordFragment.navigate(getCurrentFragment())
    }
}
package com.powersoftlab.exchange_android.ui.page.login

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.enum.LoginTypeEnum
import com.powersoftlab.exchange_android.databinding.ActivityLoginBinding
import com.powersoftlab.exchange_android.ext.getCurrentFragment
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.login.forgot.verify_phone_number.ForgotPasswordFragment
import com.powersoftlab.exchange_android.ui.page.login.register.TermRegisterFragment
import com.powersoftlab.exchange_android.ui.page.login.register.register.RegisterFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel


class LoginActivity : BaseActivity<ActivityLoginBinding>(com.powersoftlab.exchange_android.R.layout.activity_login) {

    private val loginViewModel: LoginViewModel by stateViewModel()

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
            }else{
                super.onBackPressed()
            }
        }
        //super.onBackPressed()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        getCurrentFragment().onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
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
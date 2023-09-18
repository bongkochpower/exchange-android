package com.powersoftlab.exchange_android.ui.page.main.exchange

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.ActivityExchangeBinding
import com.powersoftlab.exchange_android.ext.getCurrentFragment
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment


class ExchangeActivity : BaseActivity<ActivityExchangeBinding>(R.layout.activity_exchange) {

    companion object {
        fun open(activity: Activity) {
            val intent = Intent(activity, ExchangeActivity::class.java).apply {

            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun setUp() {
        setThisFragmentToStartDestination()
    }

    override fun listener() {
        with(binding) {
            appBarLayout.setOnBackListener {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is OnBackPressedFragment) {
            val isOverride: Boolean = currentFragment.onBackPressed()
            when {
                isOverride -> return
                currentFragment is ExchangeFragment -> super.onBackPressed()
                else -> currentFragment.findNavController().popBackStack()
            }
        }
    }

    private fun getCurrentFragment(): Fragment =
        supportFragmentManager.getCurrentFragment(R.id.fragment_container)

    private fun setThisFragmentToStartDestination() {
        ExchangeFragment.navigate(getCurrentFragment())
    }

}
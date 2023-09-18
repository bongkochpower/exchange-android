package com.powersoftlab.exchange_android.ui.page.main.topup

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.ActivityTopupBinding
import com.powersoftlab.exchange_android.ext.getCurrentFragment
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment

class TopUpActivity : BaseActivity<ActivityTopupBinding>(R.layout.activity_topup) {

    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, TopUpActivity::class.java).apply {

            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun setUp() {

    }

    override fun listener() {
        with(binding) {
            appBarLayout.setOnBackListener {
                onBackPressed()
            }
        }
    }

    override fun subscribe() {
        super.subscribe()

    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is OnBackPressedFragment) {
            val isOverride: Boolean = currentFragment.onBackPressed()
            when{
                currentFragment is TopUpFragment -> super.onBackPressed()
                isOverride -> return
                else -> currentFragment.findNavController().popBackStack()
            }
        }
    }

    private fun getCurrentFragment(): Fragment =
        supportFragmentManager.getCurrentFragment(R.id.fragment_container)


}
package com.powersoftlab.exchange_android.ui.page.main.withdraw

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.ActivityCardsBinding
import com.powersoftlab.exchange_android.databinding.ActivityWithdrawBinding
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity
import com.powersoftlab.exchange_android.ui.page.main.cards.CardsActivity
import com.powersoftlab.exchange_android.ui.page.main.cards.CardsFragment
import com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_type.WithDrawTypeFragment

class WithdrawActivity : BaseActivity<ActivityWithdrawBinding>(R.layout.activity_withdraw) {

    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, WithdrawActivity::class.java).apply {
            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun setUp() {
        with(binding) {
            replaceFragment()
        }
    }

    override fun listener() {
        with(binding) {
            appBarLayout.setOnBackListener {
                onBackPressed()
            }
        }
    }

    private fun replaceFragment() {
        val fragment = WithDrawTypeFragment.newInstance()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.commitNow()
    }


}
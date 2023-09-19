package com.powersoftlab.exchange_android.ui.page.main.cards

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.ActivityCardsBinding
import com.powersoftlab.exchange_android.ext.getCurrentFragment
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel


class CardsActivity : BaseActivity<ActivityCardsBinding>(R.layout.activity_cards) {

    private val cardViewMode : CardsViewModel by stateViewModel()

    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, CardsActivity::class.java).apply {
            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun setUp() {
        with(binding) {
            //replaceFragment()
        }
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

        with(binding){
            cardViewMode.iconLeftMenu.observe(this@CardsActivity){
                appBarLayout.setIcon(it)

            }
        }

    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is OnBackPressedFragment) {
            val isOverride: Boolean = currentFragment.onBackPressed()
            when{
                currentFragment is CardsFragment -> super.onBackPressed()
                isOverride -> return
                else -> currentFragment.findNavController().popBackStack()
            }


            /*if (isOverride) {
                return
            }else{
                super.onBackPressed()
            }*/
        }
    }

    private fun getCurrentFragment(): Fragment =
        supportFragmentManager.getCurrentFragment(R.id.fragment_container)

}
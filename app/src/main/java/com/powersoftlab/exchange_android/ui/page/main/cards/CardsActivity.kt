package com.powersoftlab.exchange_android.ui.page.main.cards

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.databinding.ActivityCardsBinding
import com.powersoftlab.exchange_android.databinding.ActivityExchangeBinding
import com.powersoftlab.exchange_android.databinding.ItemRvExchangeCurrencyBinding
import com.powersoftlab.exchange_android.ext.getCurrentFragment
import com.powersoftlab.exchange_android.ext.hideKeyboard
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ext.setTextSlideButtonEnable
import com.powersoftlab.exchange_android.ext.showKeyboard
import com.powersoftlab.exchange_android.ext.toStringFormat
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter
import com.powersoftlab.exchange_android.ui.list.viewholder.bind.ExchangeHolderHelper.initExchangeFrom
import com.powersoftlab.exchange_android.ui.list.viewholder.bind.ExchangeHolderHelper.initExchangeTo
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.login.register.TermRegisterFragment
import com.powersoftlab.exchange_android.ui.page.login.register.register.RegisterFragment
import com.powersoftlab.exchange_android.ui.page.main.cards.request_new_card.RequestNewCardFragment
import com.powersoftlab.exchange_android.ui.page.main.exchange.ExchangeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import java.util.Timer
import kotlin.concurrent.schedule


class CardsActivity : BaseActivity<ActivityCardsBinding>(R.layout.activity_cards) {

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
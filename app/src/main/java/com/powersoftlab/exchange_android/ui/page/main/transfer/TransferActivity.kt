package com.powersoftlab.exchange_android.ui.page.main.transfer

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.ActivityTransferBinding
import com.powersoftlab.exchange_android.ext.getCurrentFragment
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class TransferActivity : BaseActivity<ActivityTransferBinding>(R.layout.activity_transfer) {
    private val transferViewModel: TransferViewModel by stateViewModel()

    companion object {

        fun open(activity: Activity) {
            val intent = Intent(activity, TransferActivity::class.java).apply {
            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }
    override fun setUp() {
        with(binding){}
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

        transferViewModel.iconLeftMenu.observe(this){ iconRes ->
            binding.appBarLayout.setIcon(iconRes)
        }

        transferViewModel.titleToolbar.observe(this){
            binding.appBarLayout.showIconCenter()
        }
    }

    override fun onBackPressed() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is OnBackPressedFragment) {
            val isOverride: Boolean = currentFragment.onBackPressed()
            when{
                currentFragment is TransferFragment -> super.onBackPressed()
                isOverride -> return
                else -> currentFragment.findNavController().popBackStack()
            }
        }
    }

    private fun getCurrentFragment(): Fragment =
        supportFragmentManager.getCurrentFragment(R.id.fragment_container)

}
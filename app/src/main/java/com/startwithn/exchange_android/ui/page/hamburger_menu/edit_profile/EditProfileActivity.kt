package com.startwithn.exchange_android.ui.page.hamburger_menu.edit_profile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentTransaction
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ActivityEditProfileBinding
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import com.startwithn.exchange_android.ui.page.hamburger_menu.help.HelpActivity
import com.startwithn.exchange_android.ui.page.login.register.register.RegisterFragment

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(R.layout.activity_edit_profile) {

    companion object {
        fun open(activity: Activity) =
            ContextCompat.startActivity(activity, Intent(activity, EditProfileActivity::class.java), null)
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
        val fragment = RegisterFragment.newInstance()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.commitNow()
    }
}
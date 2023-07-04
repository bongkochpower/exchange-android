package com.startwithn.exchange_android.ui.page.main.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.ActivityMainBinding
import com.startwithn.exchange_android.ui.page.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private var navController: LiveData<NavController>? = null

    companion object {
        private const val EXTRA_NOTIFY_TYPE = "extra_notify_type"
        private const val EXTRA_NOTIFY_VALUE = "extra_notify_value"

        fun open(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java).apply {
                putExtra(EXTRA_NOTIFY_TYPE, "")
                putExtra(EXTRA_NOTIFY_VALUE, "")
            }
            ContextCompat.startActivity(activity, intent, null)
        }
    }

    override fun getExtra(bundle: Bundle) {
        super.getExtra(bundle)
        bundle.apply {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.let {
            getExtra(it)
            //handleNotification()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            //setUpBottomNavigatorBar()
        }// Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation

        //setUpBottomNavigatorBar()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun setUp() {
        initFullScreenWithStatusBar(false)
        binding.apply {
        }
    }

    override fun subscribe() {
        super.subscribe()

    }


}
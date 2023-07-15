package com.startwithn.exchange_android.ui.page.intro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.common.navigator.AppNavigator
import com.startwithn.exchange_android.databinding.ActivityIntroBinding
import com.startwithn.exchange_android.ext.fadeIn
import com.startwithn.exchange_android.ui.page.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {

    //private val firebaseViewModel: FirebaseViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            fadeIn()
            //firebaseViewModel.getFcmToken()
        }
    }

    override fun setUp() {
        initFullScreenWithStatusBar(false)
    }

    override fun listener() {

    }

    override fun subscribe() {
        super.subscribe()

        loadMasterData()


//        firebaseViewModel.isFcmSuccess.observe(this) {
//            loadMasterData()
//        }
//
//        firebaseViewModel.fcmError.observe(this) { message ->
//            message?.let {
//                showTopAlertError(it)
//            }
//        }
    }

    private fun fadeIn() {
        with(binding) {
            bgIntro.fadeIn()
        }
    }

    private fun loadMasterData() {
        //introViewModel.getCategories()

        Handler(mainLooper).postDelayed({ goToNextPage() }, 2000)
    }

    private fun goToNextPage() {
        val isRequiredLogin = appManager.getAuthToken() == null
        when {
            isRequiredLogin -> {
                goToLogin()
            }
            else -> {
                /*val isHasPin = appManager.getPin() != null
                if (isHasPin) {
                    goToPinPassword()
                } else {
                    goToMain()
                }*/
                Timber.d("token -> ${AppManager(this).getAuthToken()}")
                goToMain()
            }
        }
    }

    private fun goToMain() = AppNavigator(this).goToMain()
    private fun goToLogin() = AppNavigator(this).goToLogin()

}
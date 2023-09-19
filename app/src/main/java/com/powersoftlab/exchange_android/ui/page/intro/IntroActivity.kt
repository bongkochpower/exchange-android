package com.powersoftlab.exchange_android.ui.page.intro

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.common.navigator.AppNavigator
import com.powersoftlab.exchange_android.databinding.ActivityIntroBinding
import com.powersoftlab.exchange_android.ext.fadeIn
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.page.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber

class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {

    //private val firebaseViewModel: FirebaseViewModel by viewModel()
    private val introViewModel: IntroViewModel by stateViewModel()

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

        loadMasterData()

        //test
        if(appManager.getPin() == null){
            appManager.setPin("111111")
        }
    }

    override fun listener() {

    }

    override fun subscribe() {
        super.subscribe()

        introViewModel.subDistrictLiveData.observe(this){
            //progressDialog.dismiss()
            when (it) {
                is ResultWrapper.Loading -> {
                    //progressDialog.show(supportFragmentManager)
                }

                is ResultWrapper.Success -> {
                    goToNextPage()
                    finish()
                }

                is ResultWrapper.GenericError -> {
                    AppAlert.alert(this, it.message).show(supportFragmentManager)
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alert(this, it.message).show(supportFragmentManager)
                }

                else -> {
                    /*none*/
                }
            }
        }


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
        introViewModel.getSubDistrict()

        //Handler(mainLooper).postDelayed({ goToNextPage() }, 2000)
    }

    private fun goToNextPage() {
        val isRequiredLogin = appManager.getAuthToken() == null
        when {
            isRequiredLogin -> {
                goToLogin()
            }
            else -> {
                val isHasPin = appManager.getPin() != null
                if (isHasPin) {
                    //goToPinPassword()
                    goToMain()
                } else {
                    goToMain()
                }
                Timber.d("token -> ${AppManager(this).getAuthToken()}")
            }
        }
    }

    private fun goToMain() = AppNavigator(this).goToMain()
    private fun goToPinPassword() = AppNavigator(this).goToPinPassword()
    private fun goToLogin() = AppNavigator(this).goToLogin(true)

}
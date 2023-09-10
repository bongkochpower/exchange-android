package com.powersoftlab.exchange_android.common.navigator

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.powersoftlab.exchange_android.ui.page.hamburger_menu.edit_profile.EditProfileActivity
import com.powersoftlab.exchange_android.ui.page.hamburger_menu.help.HelpActivity
import com.powersoftlab.exchange_android.ui.page.hamburger_menu.setting.SettingsActivity
import com.powersoftlab.exchange_android.ui.page.login.LoginActivity
import com.powersoftlab.exchange_android.ui.page.main.MainActivity
import com.powersoftlab.exchange_android.ui.page.main.cards.CardsActivity
import com.powersoftlab.exchange_android.ui.page.main.exchange.ExchangeActivity
import com.powersoftlab.exchange_android.ui.page.main.history.HistoryActivity
import com.powersoftlab.exchange_android.ui.page.main.topup.TopUpActivity

class AppNavigator(private val activity: Activity) {
    /*in-app*/
    fun goToSplash() {
        //SplashScreenActivity.open(activity)
    }

    fun goToMain() {
        MainActivity.open(activity)
    }

    fun goToLogin(isClearTask: Boolean = false) {
        LoginActivity.open(activity)
        if (isClearTask) {
            activity.finishAffinity()
        }
    }

    fun goToTopUp() {
        TopUpActivity.open(activity)
    }

    fun goToExchange() {
        ExchangeActivity.open(activity)
    }

    fun goToHistory() {
        HistoryActivity.open(activity)
    }

    fun goToCards() {
        CardsActivity.open(activity)
    }

    fun goToSettings() {
        SettingsActivity.open(activity)
    }

    fun goToEditProfile() {
        EditProfileActivity.open(activity)
    }

    fun goToHelp() {
        HelpActivity.open(activity)
    }


    /*other*/
    fun goToLine(lineId: String = "Hipowershot") {
        val uri: Uri = Uri.parse("https://line.me/ti/p/@${lineId}")
        ContextCompat.startActivity(activity, Intent(Intent.ACTION_VIEW, uri), null)
    }

    fun goToWebView(url: String, isShowTitle: Boolean = true) {
//        if (activity.packageManager.getLaunchIntentForPackage("com.android.chrome") != null) {
//            try {
//                var webUrl = url
//                if (!webUrl.startsWith("http")) {
//                    webUrl = "https://$webUrl"
//                }
//                val customTabsIntent = CustomTabsIntent.Builder()
//                    .setShowTitle(isShowTitle)
//                    .setUrlBarHidingEnabled(true)
//                    .setShareState(CustomTabsIntent.SHARE_STATE_ON)
//                    .setStartAnimations(activity, R.anim.slide_up, R.anim.slide_down)
//                    .setExitAnimations(activity, R.anim.slide_up, R.anim.slide_down)
//                    .build()
//                customTabsIntent.launchUrl(activity, Uri.parse(webUrl))
//            } catch (e: Exception) {
//                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            Toast.makeText(
//                activity,
//                activity.resources.getString(R.string.message_device_not_supported),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }
}
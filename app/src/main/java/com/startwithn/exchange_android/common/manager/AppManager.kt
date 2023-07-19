package com.startwithn.exchange_android.common.manager

import android.content.Context
import com.google.gson.Gson
import com.startwithn.exchange_android.common.constant.KeyConstant
import com.startwithn.exchange_android.config.AppApplication
import com.startwithn.exchange_android.config.AppApplication.Companion.getSecureSharePreferences
import com.startwithn.exchange_android.model.response.UserModel
import me.leolin.shortcutbadger.ShortcutBadger
import timber.log.Timber

class AppManager(private val context: Context) {
    //region secure
    //region fcm
    fun setFcmToken(fcmToken: String?) {
        Timber.d("${KeyConstant.FCM_TOKEN} -> $fcmToken")
        AppApplication.getSecureSharePreferences(context).edit()
            .putString(KeyConstant.FCM_TOKEN, fcmToken)
            .apply()
    }

    fun getFcmToken(): String? {
        return AppApplication.getSecureSharePreferences(context)
            .getString(KeyConstant.FCM_TOKEN, "")
    }
    //endregion

    //region auth
    fun setAuthToken(authToken: String?) {
        Timber.d("${KeyConstant.AUTH_TOKEN} -> $authToken")
        AppApplication.getSecureSharePreferences(context).edit()
            .putString(KeyConstant.AUTH_TOKEN, authToken).apply()
    }

    fun getAuthToken(): String? {
        return AppApplication.getSecureSharePreferences(context)
            .getString(KeyConstant.AUTH_TOKEN, null)
    }
    //endregion

    //region user
    fun setUser(userModel: UserModel?) =
        getSecureSharePreferences(context).edit().putString(KeyConstant.USER, Gson().toJson(userModel))
            .apply()

    fun getUser(): UserModel? = Gson().fromJson(
        getSecureSharePreferences(context).getString(KeyConstant.USER, null),
        UserModel::class.java
    )
    //endregion

    //region notification
    fun setOpenNotification(isOpen: Boolean): Boolean {
        Timber.d("${KeyConstant.IS_OPEN_NOTIFICATION} -> $isOpen")
        AppApplication.getGeneralSharePreferences(context).edit()
            .putBoolean(KeyConstant.IS_OPEN_NOTIFICATION, isOpen)
            .apply()
        return isOpen
    }

    fun isOpenNotification(): Boolean {
        return AppApplication.getGeneralSharePreferences(context)
            .getBoolean(KeyConstant.IS_OPEN_NOTIFICATION, true)
    }

    fun addNotificationCount(count: Int = 1) {
        AppApplication.getGeneralSharePreferences(context).edit()
            .putInt(KeyConstant.NOTIFY_COUNT, getNotificationCount().plus(count)).apply()
        ShortcutBadger.applyCount(context, getNotificationCount())
    }

    fun getNotificationCount(): Int {
        return AppApplication.getGeneralSharePreferences(context)
            .getInt(KeyConstant.NOTIFY_COUNT, 0)
    }

    fun removeNotificationCount() {
        AppApplication.getGeneralSharePreferences(context).edit().remove(KeyConstant.NOTIFY_COUNT)
            .apply()
        ShortcutBadger.removeCount(context)
    }
    //endregion
    //endregion

    fun removeAll(cb: () -> Unit) {
        AppApplication.getSecureSharePreferences(context).edit().apply {
            remove(KeyConstant.FCM_TOKEN)
            remove(KeyConstant.AUTH_TOKEN)
            remove(KeyConstant.USER)
        }.apply()
        //removeNotificationCount()

        cb.invoke()
    }
}
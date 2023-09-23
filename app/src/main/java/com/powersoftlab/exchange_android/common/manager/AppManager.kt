package com.powersoftlab.exchange_android.common.manager

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.powersoftlab.exchange_android.common.constant.KeyConstant
import com.powersoftlab.exchange_android.common.enum.LoginTypeEnum
import com.powersoftlab.exchange_android.model.response.AddressAutoFillResponseModel
import com.powersoftlab.exchange_android.model.response.UserModel
import me.leolin.shortcutbadger.ShortcutBadger
import timber.log.Timber

class AppManager(private val context: Context) {

    companion object {
        private var secureSharedPreferencesInstance: SharedPreferences? = null
    }

    private fun getSecureSharePreferences(): SharedPreferences {
        if (secureSharedPreferencesInstance == null) {
            secureSharedPreferencesInstance =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    val spec = KeyGenParameterSpec.Builder(
                        MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setKeySize(256)
                        .build()

                    val masterKey =
                        MasterKey.Builder(context).setKeyGenParameterSpec(spec).build()

                    EncryptedSharedPreferences.create(
                        context,
                        "encrypted_preferences",
                        masterKey, // masterKey created above
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )
                } else {
                    return getSharePreferences()
                }
        }

        return secureSharedPreferencesInstance!!
    }

    private fun getSharePreferences(): SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)


    //region secure
    //region fcm
    fun setFcmToken(fcmToken: String?) {
        Timber.d("${KeyConstant.FCM_TOKEN} -> $fcmToken")
        getSecureSharePreferences().edit()
            .putString(KeyConstant.FCM_TOKEN, fcmToken)
            .apply()
    }

    fun getFcmToken(): String? {
        return getSecureSharePreferences().getString(KeyConstant.FCM_TOKEN, "")
    }
    //endregion

    //region auth
    fun setAuthToken(authToken: String?) {
        Timber.d("${KeyConstant.AUTH_TOKEN} -> $authToken")
        getSecureSharePreferences().edit().putString(KeyConstant.AUTH_TOKEN, authToken).apply()
    }

    fun getAuthToken(): String? {
        return getSecureSharePreferences()
            .getString(KeyConstant.AUTH_TOKEN, null)
    }
    //endregion

    //region secret key
    fun setAuthSecretKey(secretKey: String?) {
        getSecureSharePreferences().edit().putString(KeyConstant.AUTH_SECRET_KEY, secretKey).apply()
    }

    fun getAuthSecretKey(): String? {
        return getSecureSharePreferences().getString(KeyConstant.AUTH_SECRET_KEY, null)
    }
    //endregion

    //region login type
    fun setLoginType(loginType: String) {
        getSecureSharePreferences().edit().putString(KeyConstant.LOGIN_TYPE, loginType).apply()
    }

    fun getLoginType(): LoginTypeEnum {
        val type = getSecureSharePreferences().getString(KeyConstant.LOGIN_TYPE, LoginTypeEnum.APP.name)
        return LoginTypeEnum.fromName(type)
    }
    //endregion

    //region user
    fun setUser(userModel: UserModel?) =
        getSecureSharePreferences().edit().putString(KeyConstant.USER, Gson().toJson(userModel))
            .apply()

    fun getUser(): UserModel? = Gson().fromJson(
        getSecureSharePreferences().getString(KeyConstant.USER, null),
        UserModel::class.java
    )
    //endregion
    //endregion

    //region notification
    fun setOpenNotification(isOpen: Boolean): Boolean {
        Timber.d("${KeyConstant.IS_OPEN_NOTIFICATION} -> $isOpen")
        getSharePreferences().edit()
            .putBoolean(KeyConstant.IS_OPEN_NOTIFICATION, isOpen)
            .apply()
        return isOpen
    }

    fun isOpenNotification(): Boolean {
        return getSharePreferences()
            .getBoolean(KeyConstant.IS_OPEN_NOTIFICATION, true)
    }

    fun addNotificationCount(count: Int = 1) {
        getSharePreferences().edit()
            .putInt(KeyConstant.NOTIFY_COUNT, getNotificationCount().plus(count)).apply()
        ShortcutBadger.applyCount(context, getNotificationCount())
    }

    fun getNotificationCount(): Int {
        return getSharePreferences().getInt(KeyConstant.NOTIFY_COUNT, 0)
    }

    fun removeNotificationCount() {
        getSharePreferences().edit().remove(KeyConstant.NOTIFY_COUNT)
            .apply()
        ShortcutBadger.removeCount(context)
    }
    //endregion

    //region address
    fun setSubDistricts(list: List<AddressAutoFillResponseModel.SubDistrictResponse>?) =
        getSharePreferences().edit()
            .putString(KeyConstant.SUB_DISTRICTS, Gson().toJson(list)).apply()

    fun getSubDistricts(): List<AddressAutoFillResponseModel.SubDistrictResponse>? = Gson().fromJson(
        getSharePreferences().getString(KeyConstant.SUB_DISTRICTS, null),
        object : TypeToken<List<AddressAutoFillResponseModel.SubDistrictResponse>?>() {}.type
    )
    //endregion

    fun setPin(pin: String?) {
        getSecureSharePreferences().edit().putString(KeyConstant.PIN_AUTH, pin).apply()
    }
    fun getPin(): String? = getSecureSharePreferences().getString(KeyConstant.PIN_AUTH, "111111")


    fun removeAll(cb: () -> Unit) {
        getSecureSharePreferences().edit().apply {
            remove(KeyConstant.FCM_TOKEN)
            remove(KeyConstant.AUTH_TOKEN)
            remove(KeyConstant.USER)
            remove(KeyConstant.LOGIN_TYPE)
            remove(KeyConstant.AUTH_SECRET_KEY)
        }.apply()
        //removeNotificationCount()

        cb.invoke()
    }
}
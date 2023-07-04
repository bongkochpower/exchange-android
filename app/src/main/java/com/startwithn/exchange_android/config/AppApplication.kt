package com.startwithn.exchange_android.config

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.startwithn.exchange_android.BuildConfig
import com.startwithn.exchange_android.di.factoryModule
import com.startwithn.exchange_android.di.singleModule
import com.startwithn.exchange_android.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class AppApplication : MultiDexApplication() {
    companion object {
        private var secureSharedPreferencesInstance: SharedPreferences? = null
        fun getSecureSharePreferences(context: Context): SharedPreferences {
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
                        getGeneralSharePreferences(context)
                    }
            }

            return secureSharedPreferencesInstance!!
        }

        fun getGeneralSharePreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Firebase
        //FirebaseRemoteConfig.getInstance().setDefaultsAsync(R.xml.remote_config_defaults)

        // Koin
        startKoin {
            androidContext(applicationContext)
            modules(listOf(singleModule, factoryModule, viewModelModule))
            androidLogger()
        }

        // Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        /*disable night mode*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
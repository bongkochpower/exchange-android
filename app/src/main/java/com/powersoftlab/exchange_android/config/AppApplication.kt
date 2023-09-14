package com.powersoftlab.exchange_android.config

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.powersoftlab.exchange_android.BuildConfig
import com.powersoftlab.exchange_android.di.factoryModule
import com.powersoftlab.exchange_android.di.singleModule
import com.powersoftlab.exchange_android.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class AppApplication : MultiDexApplication() {
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
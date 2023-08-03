package com.powersoftlab.exchange_android.di

import com.powersoftlab.exchange_android.common.manager.AppManager
import org.koin.dsl.module

var singleModule = module {
    single { AppManager(get()) }
}
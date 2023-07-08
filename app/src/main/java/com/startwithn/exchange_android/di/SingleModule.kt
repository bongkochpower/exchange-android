package com.startwithn.exchange_android.di

import com.startwithn.exchange_android.common.manager.AppManager
import org.koin.dsl.module

var singleModule = module {
    single { AppManager(get()) }
}
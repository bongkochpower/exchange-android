package com.startwithn.exchange_android.di

import android.content.Context
import com.startwithn.exchange_android.BuildConfig
import com.startwithn.exchange_android.network.builder.RetrofitBuilder
import com.startwithn.exchange_android.repository.remote.UserRemoteRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

var factoryModule = module {

    /*api*/
    factory { RetrofitBuilder(get()).api() }

    /*repository*/
    /*remote*/
    factory { UserRemoteRepository(androidContext(), get()) }

}

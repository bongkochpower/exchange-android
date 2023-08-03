package com.powersoftlab.exchange_android.di

import com.powersoftlab.exchange_android.network.builder.RetrofitBuilder
import com.powersoftlab.exchange_android.repository.remote.AppRemoteRepository
import com.powersoftlab.exchange_android.repository.remote.UserRemoteRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

var factoryModule = module {

    /*api*/
    factory { RetrofitBuilder(get()).api() }

    /*repository*/
    /*remote*/
    factory { UserRemoteRepository(androidContext(), get()) }
    factory { AppRemoteRepository(androidContext(), get()) }

}

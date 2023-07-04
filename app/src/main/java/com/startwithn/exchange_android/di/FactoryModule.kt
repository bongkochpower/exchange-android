package com.startwithn.exchange_android.di

import android.content.Context
import com.startwithn.exchange_android.BuildConfig
import com.startwithn.exchange_android.network.builder.RetrofitBuilder
import org.koin.dsl.module

var factoryModule = module {

    /*api*/
    factory { provideRetrofitBuilder(get()).getAppAPI() }

//    /*repository*/
//    /*local*/

    /*remote*/
    //factory { AuthorizationRemoteRepository(get(), get()) }

}

fun provideRetrofitBuilder(context: Context):RetrofitBuilder= RetrofitBuilder(context, BuildConfig.BASE_URL,BuildConfig.APPLICATION_ID,BuildConfig.VERSION_NAME)
package com.startwithn.exchange_android.di
import com.startwithn.exchange_android.ui.page.login.LoginViewModel
import com.startwithn.exchange_android.ui.page.login.register.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var viewModelModule = module {
    viewModel { LoginViewModel(get(),get()) }
    viewModel { RegisterViewModel(get(),get()) }

}
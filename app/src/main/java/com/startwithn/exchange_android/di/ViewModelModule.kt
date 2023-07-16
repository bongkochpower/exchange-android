package com.startwithn.exchange_android.di
import com.startwithn.exchange_android.ui.page.login.LoginViewModel
import com.startwithn.exchange_android.ui.page.login.register.register.RegisterViewModel
import com.startwithn.exchange_android.ui.page.main.MainViewModel
import com.startwithn.exchange_android.ui.page.main.history.HistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var viewModelModule = module {
    viewModel { LoginViewModel(get(),get()) }
    viewModel { RegisterViewModel(get(),get()) }
    viewModel { MainViewModel(get(),get(),get(),get()) }
    viewModel { HistoryViewModel(get(),get()) }

}
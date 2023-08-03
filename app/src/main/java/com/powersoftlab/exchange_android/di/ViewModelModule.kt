package com.powersoftlab.exchange_android.di
import com.powersoftlab.exchange_android.ui.page.login.LoginViewModel
import com.powersoftlab.exchange_android.ui.page.login.register.register.RegisterViewModel
import com.powersoftlab.exchange_android.ui.page.main.MainViewModel
import com.powersoftlab.exchange_android.ui.page.main.exchange.ExchangeViewModel
import com.powersoftlab.exchange_android.ui.page.main.history.HistoryViewModel
import com.powersoftlab.exchange_android.ui.page.main.topup.TopUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var viewModelModule = module {
    viewModel { LoginViewModel(get(),get()) }
    viewModel { RegisterViewModel(get(),get()) }
    viewModel { MainViewModel(get(),get(),get(),get()) }
    viewModel { HistoryViewModel(get(),get()) }
    viewModel { TopUpViewModel(get(),get()) }
    viewModel { ExchangeViewModel(get(),get(),get()) }

}
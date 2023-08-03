package com.powersoftlab.exchange_android.ui.page.main.exchange

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.common.other.SingleLiveEvent
import com.powersoftlab.exchange_android.model.body.ExchangeRequestModel
import com.powersoftlab.exchange_android.model.response.ExchangeCalculateResponse
import com.powersoftlab.exchange_android.model.response.TopUpResponse
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.AppRemoteRepository
import kotlinx.coroutines.launch

class ExchangeViewModel(
    private val appRemoteRepository: AppRemoteRepository,
    private val appManager: AppManager,
    private val state: SavedStateHandle
) : ViewModel() {

    val exchangeLiveData = SingleLiveEvent<ResultWrapper<TopUpResponse>>()
    val exchangeCalculateLiveData = SingleLiveEvent<ResultWrapper<ExchangeCalculateResponse>>()

    var currencyFrom: UserModel.CustomerBalance?
        get() = state["currency_from"]
        set(value) = state.set("currency_from", value)

    var currencyTo: UserModel.CustomerBalance?
        get() = state["currency_to"]
        set(value) = state.set("currency_to", value)

    var amount: Double?
        get() = state["amount"]
        set(value) = state.set("amount", value)

    fun getCurrencyForm() : MutableList<UserModel.CustomerBalance>{
        return appManager.getUser()?.customerBalances ?: mutableListOf()
    }

    fun exchange() {
        viewModelScope.launch {
            exchangeLiveData.value = ResultWrapper.Loading
            val result = appRemoteRepository.exchange(
                ExchangeRequestModel(
                    currencyFormId = currencyFrom?.id  ?: 0,
                    currencyToId = currencyTo?.id ?: 0,
                    amount = amount ?: 0.0
                )
            )
            exchangeLiveData.value = result
        }
    }

    fun exchangeCalculate() {
        viewModelScope.launch {
            exchangeCalculateLiveData.value = ResultWrapper.Loading
            val request = ExchangeRequestModel(
                currencyFormId = currencyFrom?.id  ?: 1,
                currencyToId = currencyTo?.id ?: 1,
                amount = amount ?: 1.0
            )
            val result = appRemoteRepository.exchangeCalculate(request)
            exchangeCalculateLiveData.value = result
        }
    }
}
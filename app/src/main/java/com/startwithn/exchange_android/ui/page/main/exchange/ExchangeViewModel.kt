package com.startwithn.exchange_android.ui.page.main.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.common.other.SingleLiveEvent
import com.startwithn.exchange_android.model.body.ExchangeRequestModel
import com.startwithn.exchange_android.model.response.ExchangeCalculateResponse
import com.startwithn.exchange_android.model.response.TopUpResponse
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.repository.remote.AppRemoteRepository
import kotlinx.coroutines.launch

class ExchangeViewModel(
    private val appRemoteRepository: AppRemoteRepository,
    private val appManager: AppManager,
    private val state: SavedStateHandle
) : ViewModel() {

    //private val currencyFrom : MutableList<UserModel.CustomerBalance> = mutableListOf()
    val exchangeLiveData = SingleLiveEvent<ResultWrapper<TopUpResponse>>()
    val exchangeCalculateLiveData = SingleLiveEvent<ResultWrapper<ExchangeCalculateResponse>>()

    var currencyFrom: Int?
        get() = state["currency_from"]
        set(value) = state.set("currency_from", value)

    var currencyTo: Int?
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
                    currencyFormId = 1,
                    currencyToId = 3,
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
                currencyFormId = 1,
                currencyToId = 3,
                amount = amount ?: 0.0
            )
            val result = appRemoteRepository.exchangeCalculate(request)
            exchangeCalculateLiveData.value = result
        }
    }
}
package com.startwithn.exchange_android.ui.page.main.topup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startwithn.exchange_android.common.other.SingleLiveEvent
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.response.TopUpResponse
import com.startwithn.exchange_android.model.response.TransactionsModel
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.repository.remote.AppRemoteRepository
import kotlinx.coroutines.launch

class TopUpViewModel(
    private val appRemoteRepository: AppRemoteRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    val topUpLiveData = SingleLiveEvent<ResultWrapper<TopUpResponse>>()
//    val topUpResultLiveData: MutableLiveData<MutableList<TransactionsModel>>
//        get() = state.getLiveData("topup")
//
//    private var topUp: MutableList<TransactionsModel>
//        get() = state["topup"] ?: mutableListOf()
//        set(value) = state.set("topup", value)

    fun topUp(amount : Double) {
            viewModelScope.launch {
                topUpLiveData.value = ResultWrapper.Loading
                val result = appRemoteRepository.topUp(amount = amount)
                topUpLiveData.value = result
            }
    }

}
package com.startwithn.exchange_android.ui.page.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.common.other.SingleLiveEvent
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.body.LoginRequestModel
import com.startwithn.exchange_android.model.response.AccessTokenModel
import com.startwithn.exchange_android.model.response.TransactionsModel
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.repository.remote.AppRemoteRepository
import com.startwithn.exchange_android.repository.remote.UserRemoteRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRemoteRepository: UserRemoteRepository,
    private val appRemoteRepository: AppRemoteRepository,
    private val appManager: AppManager,
    private val state: SavedStateHandle
) : ViewModel() {

    private val me: MutableLiveData<ResultWrapper<BaseResponseModel<UserModel>>> = MutableLiveData()

    val transactionRequestLiveData = SingleLiveEvent<ResultWrapper<BaseResponseModel<List<TransactionsModel>>>>()
    val transactionResultLiveData: MutableLiveData<MutableList<TransactionsModel>>
        get() = state.getLiveData("transaction_list")
    private var transactionList: MutableList<TransactionsModel>
        get() = state["transaction_list"] ?: mutableListOf()
        set(value) = state.set("transaction_list", value)

    fun getMe() {
        me.value = ResultWrapper.Loading
        viewModelScope.launch {
            me.value = userRemoteRepository.me()
        }
    }

    fun getMeResult() = me

    fun getLastTransaction() {
        viewModelScope.launch {
            transactionRequestLiveData.value = ResultWrapper.Loading
            val result = appRemoteRepository.getLastTransactions()
            transactionRequestLiveData.value = result
            if (result is ResultWrapper.Success) {
                val list = result.response.data?.toMutableList() ?: mutableListOf()
                transactionList = list
            }
        }
    }

}
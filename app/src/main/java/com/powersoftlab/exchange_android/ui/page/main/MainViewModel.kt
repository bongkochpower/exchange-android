package com.powersoftlab.exchange_android.ui.page.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.common.other.SingleLiveEvent
import com.powersoftlab.exchange_android.model.base.BaseResponseModel
import com.powersoftlab.exchange_android.model.response.TransactionsModel
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.AppRemoteRepository
import com.powersoftlab.exchange_android.repository.remote.UserRemoteRepository
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
            val result = userRemoteRepository.me()
            if(result is ResultWrapper.Success){
                appManager.setUser(result.response.data)
            }
            me.value = result
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
package com.startwithn.exchange_android.ui.page.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.common.other.SingleLiveEvent
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.body.LoginRequestModel
import com.startwithn.exchange_android.model.response.AccessTokenModel
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.repository.remote.UserRemoteRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRemoteRepository: UserRemoteRepository,
                     private val appManager: AppManager
) : ViewModel() {

    private val me : MutableLiveData<ResultWrapper<BaseResponseModel<UserModel>>> = MutableLiveData()

    fun getMe(){
        me.value = ResultWrapper.Loading
        viewModelScope.launch {
            me.value = userRemoteRepository.me()
        }
    }
    fun getMeResult() = me
}
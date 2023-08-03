package com.powersoftlab.exchange_android.ui.page.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.common.other.SingleLiveEvent
import com.powersoftlab.exchange_android.model.base.BaseResponseModel
import com.powersoftlab.exchange_android.model.body.LoginRequestModel
import com.powersoftlab.exchange_android.model.response.AccessTokenModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.UserRemoteRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRemoteRepository: UserRemoteRepository,
                     private val appManager: AppManager
) : ViewModel() {

    val loginLiveData = SingleLiveEvent<ResultWrapper<BaseResponseModel<AccessTokenModel>>>()
    val iconLeftMenu = MutableLiveData<Int>()

    fun login(request: LoginRequestModel) {
        viewModelScope.launch {
            loginLiveData.value = ResultWrapper.Loading
            val result = userRemoteRepository.login(request)
            if(result is ResultWrapper.Success){
                result.response.data?.let { token ->
                    appManager.setAuthToken(token.accessToken)
                }
            }
            loginLiveData.value = result
        }
    }

    fun setIcon(iconRes : Int){
        iconLeftMenu.value = iconRes
    }




}
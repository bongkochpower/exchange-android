package com.startwithn.exchange_android.ui.page.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.common.other.SingleLiveEvent
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.body.LoginRequestModel
import com.startwithn.exchange_android.model.body.RegisterRequestModel
import com.startwithn.exchange_android.model.response.AccessTokenModel
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.repository.remote.UserRemoteRepository
import com.startwithn.exchange_android.ui.page.login.register.register.RegisterFragment
import kotlinx.coroutines.launch

class LoginViewModel(private val userRemoteRepository: UserRemoteRepository,
                     private val appManager: AppManager
) : ViewModel() {

    val loginLiveData = SingleLiveEvent<ResultWrapper<BaseResponseModel<AccessTokenModel>>>()

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
}
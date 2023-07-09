package com.startwithn.exchange_android.ui.page.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.common.other.SingleLiveEvent
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.body.LoginRequestModel
import com.startwithn.exchange_android.model.body.RegisterRequestModel
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.repository.remote.UserRemoteRepository
import com.startwithn.exchange_android.ui.page.login.register.register.RegisterFragment
import kotlinx.coroutines.launch

class LoginViewModel(private val userRemoteRepository: UserRemoteRepository,
                     private val appManager: AppManager
) : ViewModel() {

    val loginLiveData = SingleLiveEvent<ResultWrapper<BaseResponseModel<UserModel>>>()
    /*



    val customerIdFromRegisterLiveData = SingleLiveEvent<String>()

    fun setCustomerId(customerId:String?){
        customerIdFromRegisterLiveData.value =customerId
    }*/

    fun login(request: LoginRequestModel) {
        viewModelScope.launch {
            loginLiveData.value = ResultWrapper.Loading
            loginLiveData.value = userRemoteRepository.login(request)
        }
    }

}
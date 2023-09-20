package com.powersoftlab.exchange_android.ui.page.main.withdraw.auth_with_bio

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.common.other.SingleLiveEvent
import com.powersoftlab.exchange_android.model.response.PinResponseModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.UserRemoteRepository
import kotlinx.coroutines.launch

class AuthWithBioViewModel(
    private val appManager: AppManager,
    private val userRemoteRepository: UserRemoteRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    val authPinLiveData : SingleLiveEvent<ResultWrapper<PinResponseModel>> = SingleLiveEvent()
    private val DELEY = 1000L


//    fun setPin(pin : String){
//        authPin.value = pin
//    }

    private fun getPin(): String? = appManager.getPin()
    private fun isPinMatch(inputPin: String): Boolean = inputPin.equals(getPin(), true)

    fun authPin(pin : String) {
        viewModelScope.launch {
            authPinLiveData.value = ResultWrapper.Loading
            //delay(DELEY)
//            val result = if(isPinMatch(pin)) ResultWrapper.Success<String>("111111") else ResultWrapper.GenericError(1,"")
            val result = userRemoteRepository.checkPin(pin)
            authPinLiveData.value = result
        }
    }
}
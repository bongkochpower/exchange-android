package com.startwithn.exchange_android.ui.page.login.register.register

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startwithn.exchange_android.common.other.SingleLiveEvent
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.body.RegisterRequestModel
import com.startwithn.exchange_android.model.response.UploadResponseModel
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.repository.remote.UserRemoteRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class RegisterViewModel(private val userRemoteRepository: UserRemoteRepository, private val state: SavedStateHandle) :
    ViewModel() {

    /*var selectedLocation: Location?
        get() = state["selected_location"]
        set(value) = state.set("selected_location", value)*/

    private val registerLiveData = SingleLiveEvent<ResultWrapper<RegisterRequestModel>>()
    private val uploadProfile: MutableLiveData<ResultWrapper<UploadResponseModel>> = MutableLiveData()

    fun register(request: RegisterRequestModel) {
        viewModelScope.launch {
            registerLiveData.value = ResultWrapper.Loading
            registerLiveData.value = userRemoteRepository.register(request)
        }
    }
    fun registerResult() = registerLiveData

    fun updateProfile(avatar: MultipartBody.Part) {
        uploadProfile.value = ResultWrapper.Loading
        viewModelScope.launch {
            uploadProfile.value = userRemoteRepository.uploadAvatar(avatar)
            //uploadProfile.postValue(null)
        }
    }
    fun updateProfileResult() = uploadProfile

}
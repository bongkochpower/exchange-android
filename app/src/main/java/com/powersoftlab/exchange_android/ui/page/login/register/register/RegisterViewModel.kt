package com.powersoftlab.exchange_android.ui.page.login.register.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powersoftlab.exchange_android.common.other.SingleLiveEvent
import com.powersoftlab.exchange_android.model.body.RegisterRequestModel
import com.powersoftlab.exchange_android.model.response.RegisterResponseModel
import com.powersoftlab.exchange_android.model.response.UploadResponseModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.UserRemoteRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class RegisterViewModel(private val userRemoteRepository: UserRemoteRepository, private val state: SavedStateHandle) :
    ViewModel() {

    /*var selectedLocation: Location?
        get() = state["selected_location"]
        set(value) = state.set("selected_location", value)*/
    var selectedIdCardImage : String?
        get() = state["selected_id_card"]
        set(value) = state.set("selected_id_card",value)

    private val registerLiveData = SingleLiveEvent<ResultWrapper<RegisterResponseModel>>()
    private val uploadProfile: MutableLiveData<ResultWrapper<UploadResponseModel>> = MutableLiveData()
    val uploadIdCardImage: SingleLiveEvent<ResultWrapper<UploadResponseModel>> = SingleLiveEvent()
    val updateProfileLiveData = SingleLiveEvent<ResultWrapper<Any>>()

    fun register(request: RegisterRequestModel) {
        viewModelScope.launch {
            registerLiveData.value = ResultWrapper.Loading
            registerLiveData.value = userRemoteRepository.register(request)
        }
    }
    fun registerResult() = registerLiveData

    fun uploadImageProfile(avatar: MultipartBody.Part) {
        uploadProfile.value = ResultWrapper.Loading
        viewModelScope.launch {
            uploadProfile.value = userRemoteRepository.uploadAvatar(avatar)
        }
    }
    fun uploadImageProfileResult() = uploadProfile

    fun uploadIdCardImage(image : MultipartBody.Part){
        uploadIdCardImage.value = ResultWrapper.Loading
        viewModelScope.launch {
            uploadIdCardImage.value = userRemoteRepository.uploadAvatar(image)
        }
    }

    fun updateProfile(userId : Int , request: RegisterRequestModel){
        updateProfileLiveData.value = ResultWrapper.Loading
        viewModelScope.launch {
            updateProfileLiveData.value = userRemoteRepository.updateProfile(userId, request)
        }
    }

}
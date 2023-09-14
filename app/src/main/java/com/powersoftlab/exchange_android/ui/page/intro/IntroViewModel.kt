package com.powersoftlab.exchange_android.ui.page.intro

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linecorp.linesdk.LineProfile
import com.linecorp.linesdk.Scope
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginApi
import com.linecorp.linesdk.auth.LineLoginResult
import com.powersoftlab.exchange_android.common.enum.LoginTypeEnum
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.common.other.SingleLiveEvent
import com.powersoftlab.exchange_android.model.base.BaseResponseModel
import com.powersoftlab.exchange_android.model.body.LoginRequestModel
import com.powersoftlab.exchange_android.model.body.LoginSocialRequestModel
import com.powersoftlab.exchange_android.model.response.AccessTokenModel
import com.powersoftlab.exchange_android.model.response.AddressAutoFillResponseModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.AppRemoteRepository
import com.powersoftlab.exchange_android.repository.remote.UserRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class IntroViewModel(private val appRemoteRepository: AppRemoteRepository,
                     private val appManager: AppManager,
                     private val state: SavedStateHandle
) : ViewModel() {

    val subDistrictLiveData = SingleLiveEvent<ResultWrapper<BaseResponseModel<List<AddressAutoFillResponseModel.SubDistrictResponse>>>>()

    fun getSubDistrict() {
        viewModelScope.launch {
            subDistrictLiveData.value = ResultWrapper.Loading
            val result = appRemoteRepository.getSubDistricts()
            if(result is ResultWrapper.Success){
                result.response.data?.let { subDistricts ->
                    appManager.setSubDistricts(subDistricts)
                }
            }
            subDistrictLiveData.value = result
        }
    }


    //endregion






}
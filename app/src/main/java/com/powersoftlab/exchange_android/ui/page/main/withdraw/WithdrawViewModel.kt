package com.powersoftlab.exchange_android.ui.page.main.withdraw

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.common.other.SingleLiveEvent
import com.powersoftlab.exchange_android.model.base.BaseResponseModel
import com.powersoftlab.exchange_android.model.body.WithdrawRequestModel
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.model.response.WithdrawResponseModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.AppRemoteRepository
import kotlinx.coroutines.launch

class WithdrawViewModel(
    private val appManager: AppManager,
    private val appRemoteRepository: AppRemoteRepository,
    private val state: SavedStateHandle
):ViewModel() {

    val iconLeftMenu = MutableLiveData<Int>()
    val countryRequestLiveData = SingleLiveEvent<ResultWrapper<BaseResponseModel<List<UserModel.CountryModel>>>>()
    val countryResultLiveData: MutableLiveData<MutableList<UserModel.CountryModel>>
        get() = state.getLiveData("country_list")
    private var countryList: MutableList<UserModel.CountryModel>
        get() = state["country_list"] ?: mutableListOf()
        set(value) = state.set("country_list", value)

    val shopByIdLiveData = SingleLiveEvent<ResultWrapper<BaseResponseModel<List<UserModel.ShopModel>>>>()
    val withdrawLiveData = SingleLiveEvent<ResultWrapper<WithdrawResponseModel>>()

    var selectedCurrency : UserModel.CustomerBalance?
        get() = state["selected_currency"]
        set(value) = state.set("selected_currency",value)

    var selectedCountry : UserModel.CountryModel?
        get() = state["selected_country"]
        set(value) = state.set("selected_country",value)

    var selectedShop : UserModel.ShopModel?
        get() = state["selected_shop"]
        set(value) = state.set("selected_shop",value)




    fun setIcon(iconRes : Int){
        iconLeftMenu.value = iconRes
    }

    fun getCurrency() : MutableList<UserModel.CustomerBalance>{
        return appManager.getUser()?.customerBalances ?: mutableListOf()
    }

    fun getCountryList() {
        viewModelScope.launch {
            countryRequestLiveData.value = ResultWrapper.Loading
            val result = appRemoteRepository.getCountry()
            countryRequestLiveData.value = result
            if (result is ResultWrapper.Success) {
                val list = result.response.data?.toMutableList() ?: mutableListOf()
                countryList = list
            }
        }
    }

    fun getShopById() {
        viewModelScope.launch {
            shopByIdLiveData.value = ResultWrapper.Loading
            val result = appRemoteRepository.getShopByCountryId(
                countryId = selectedCountry?.id ?: 0
            )
            shopByIdLiveData.value = result
        }
    }

    fun withdraw(amount : Double) {
        viewModelScope.launch {
            withdrawLiveData.value = ResultWrapper.Loading
            val result = appRemoteRepository.withdraw(
                WithdrawRequestModel(
                    currencyId = selectedCurrency?.id ?: 0,
                    amount = amount,
                    shopId = selectedShop?.id ?: 0
                )
            )
            withdrawLiveData.value = result
        }
    }
}
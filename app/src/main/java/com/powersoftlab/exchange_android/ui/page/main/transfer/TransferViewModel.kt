package com.powersoftlab.exchange_android.ui.page.main.transfer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.common.other.SingleLiveEvent
import com.powersoftlab.exchange_android.model.base.BaseResponseModel
import com.powersoftlab.exchange_android.model.body.TransferRequestModel
import com.powersoftlab.exchange_android.model.response.TransferResponseModel
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.model.response.WalletDetailResponseModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.AppRemoteRepository
import kotlinx.coroutines.launch

class TransferViewModel(
    private val appManager: AppManager,
    private val appRemoteRepository: AppRemoteRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    val profileByWalletIdRequestLiveData = SingleLiveEvent<ResultWrapper<BaseResponseModel<WalletDetailResponseModel>>>()
    val transferRequestLiveData = SingleLiveEvent<ResultWrapper<BaseResponseModel<TransferResponseModel>>>()

    val iconLeftMenu = MutableLiveData<Int>()
    val titleToolbar = MutableLiveData<String>()

    var selectedCurrency: UserModel.CustomerBalance?
        get() = state["selected_currency"]
        set(value) = state.set("selected_currency", value)

    var inputMoneyTransfer: Double?
        get() = state["money_transfer"]
        set(value) = state.set("money_transfer", value)

    var walletId: String?
        get() = state["wallet_id"]
        set(value) = state.set("wallet_id", value)

    var walletProfile: WalletDetailResponseModel?
        get() = state["wallet_information"]
        set(value) = state.set("wallet_information", value)


    fun setIcon(iconRes: Int) {
        iconLeftMenu.value = iconRes
    }

    fun setTitle(title: String) {
        titleToolbar.value = title
    }

    fun getCurrency(): MutableList<UserModel.CustomerBalance> {
        return appManager.getUser()?.customerBalances ?: mutableListOf()
    }

    fun getProfileByWalletId() {
        viewModelScope.launch {
            profileByWalletIdRequestLiveData.value = ResultWrapper.Loading
            val result = appRemoteRepository.profileByWalletId(
                walletId = walletId.orEmpty(),
            )
            if(result is ResultWrapper.Success){
                walletProfile = result.response.data
            }
            profileByWalletIdRequestLiveData.value = result

        }
    }

    fun transfer() {
        viewModelScope.launch {
            transferRequestLiveData.value = ResultWrapper.Loading
            val result = appRemoteRepository.transfer(
                requestModel = TransferRequestModel(
                    currencyId = selectedCurrency?.id ?: 0,
                    amount = inputMoneyTransfer ?: 0.0,
                    walletId = walletId.orEmpty()
                )
            )
            transferRequestLiveData.value = result
        }
    }

}
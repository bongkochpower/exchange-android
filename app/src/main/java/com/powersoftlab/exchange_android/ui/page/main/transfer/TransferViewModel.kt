package com.powersoftlab.exchange_android.ui.page.main.transfer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.repository.remote.AppRemoteRepository

class TransferViewModel(
    private val appManager: AppManager,
    private val appRemoteRepository: AppRemoteRepository,
    private val state: SavedStateHandle
):ViewModel() {

    val iconLeftMenu = MutableLiveData<Int>()
    val titleToolbar = MutableLiveData<String>()

    var selectedCurrency : UserModel.CustomerBalance?
        get() = state["selected_currency"]
        set(value) = state.set("selected_currency",value)


    fun setIcon(iconRes : Int){
        iconLeftMenu.value = iconRes
    }

    fun setTitle(title : String){
        titleToolbar.value = title
    }

    fun getCurrency() : MutableList<UserModel.CustomerBalance>{
        return appManager.getUser()?.customerBalances ?: mutableListOf()
    }

}
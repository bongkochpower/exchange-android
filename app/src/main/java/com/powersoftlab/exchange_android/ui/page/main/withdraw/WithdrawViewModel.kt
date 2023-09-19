package com.powersoftlab.exchange_android.ui.page.main.withdraw

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.model.response.UserModel

class WithdrawViewModel(
    private val appManager: AppManager,
    private val state: SavedStateHandle
):ViewModel() {
    val iconLeftMenu = MutableLiveData<Int>()

    fun setIcon(iconRes : Int){
        iconLeftMenu.value = iconRes
    }

    fun getCurrency() : MutableList<UserModel.CustomerBalance>{
        return appManager.getUser()?.customerBalances ?: mutableListOf()
    }
}
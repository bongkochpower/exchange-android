package com.powersoftlab.exchange_android.ui.page.main.cards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardsViewModel(

) : ViewModel(){

    val iconLeftMenu = MutableLiveData<Int>()

    fun setIcon(iconRes : Int){
        iconLeftMenu.value = iconRes
    }
}
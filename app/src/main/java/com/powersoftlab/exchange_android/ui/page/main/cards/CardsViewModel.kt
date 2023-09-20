package com.powersoftlab.exchange_android.ui.page.main.cards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powersoftlab.exchange_android.common.other.SingleLiveEvent
import com.powersoftlab.exchange_android.model.base.BaseResponseModel
import com.powersoftlab.exchange_android.model.body.RequestNewCardRequestModel
import com.powersoftlab.exchange_android.model.response.CardsResponseModel
import com.powersoftlab.exchange_android.model.response.RequestNewCardResponseModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.AppRemoteRepository
import kotlinx.coroutines.launch

class CardsViewModel(
    private val appRemoteRepository: AppRemoteRepository,
) : ViewModel(){

    val iconLeftMenu = MutableLiveData<Int>()
    private val cardsLiveData: MutableLiveData<ResultWrapper<BaseResponseModel<List<CardsResponseModel>>>> = MutableLiveData()
    private val requestNewCardLiveData = SingleLiveEvent<ResultWrapper<RequestNewCardResponseModel>>()


    fun setIcon(iconRes : Int){
        iconLeftMenu.value = iconRes
    }

    fun getCards() {
        cardsLiveData.value = ResultWrapper.Loading
        viewModelScope.launch {
            val result = appRemoteRepository.getCards()
            cardsLiveData.value = result
        }
    }
    fun getCardsResult() = cardsLiveData

    fun requestNewCard(request: RequestNewCardRequestModel) {
        viewModelScope.launch {
            requestNewCardLiveData.value = ResultWrapper.Loading
            requestNewCardLiveData.value = appRemoteRepository.requestNewCard(request)
        }
    }
    fun requestNewCardResult() = requestNewCardLiveData
}
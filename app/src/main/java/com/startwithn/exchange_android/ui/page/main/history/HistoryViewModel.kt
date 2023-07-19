package com.startwithn.exchange_android.ui.page.main.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startwithn.exchange_android.common.other.SingleLiveEvent
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.response.TransactionsModel
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.repository.remote.AppRemoteRepository
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val appRemoteRepository: AppRemoteRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    val historyRequestLiveData = MutableLiveData<ResultWrapper<BaseResponseModel<List<TransactionsModel>>>>()

    val historyResultLiveData: MutableLiveData<MutableList<TransactionsModel>>
        get() = state.getLiveData("history_list")

    private var historyList: MutableList<TransactionsModel>
        get() = state["history_list"] ?: mutableListOf()
        set(value) = state.set("history_list", value)

    var page: Int?
        get() = state["page"]
        set(value) = state.set("page", value)

    private val rowsPerPage: Int = 30

    init {
        getLastHistory()
    }

    fun getHistory(nextPage: Int? = 1, dateForm: String, dateTo: String) {
        page = nextPage
        page?.let {
            viewModelScope.launch {
                historyRequestLiveData.value = ResultWrapper.Loading
                val result = appRemoteRepository.getHistoryTransactions(page = page!!, limit = rowsPerPage, from = dateForm, to = dateTo)
                historyRequestLiveData.value = result
                if (result is ResultWrapper.Success) {
                    val list = result.response.data?.toMutableList() ?: mutableListOf()
                    historyList = when (page) {
                        1 -> {
                            list
                        }

                        else -> {
                            val cloneList: MutableList<TransactionsModel> = historyList
                            cloneList.addAll(list)
                            cloneList
                        }
                    }
                    page = result.response.getNextPage()
                }
            }
        }
    }

    private fun getLastHistory() {
        viewModelScope.launch {
            historyRequestLiveData.value = ResultWrapper.Loading
            val result = appRemoteRepository.getLastTransactions()
            historyRequestLiveData.value = result
            if(result is ResultWrapper.Success){
                val list = result.response.data?.toMutableList() ?: mutableListOf()
                historyList = list
            }
        }
    }


}
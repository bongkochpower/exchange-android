package com.startwithn.exchange_android.repository.remote

import android.content.Context
import android.util.Log
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.ext.toSHA256
import com.startwithn.exchange_android.model.base.BaseResponseModel
import com.startwithn.exchange_android.model.body.ExchangeRequestModel
import com.startwithn.exchange_android.model.body.LoginRequestModel
import com.startwithn.exchange_android.model.body.RegisterRequestModel
import com.startwithn.exchange_android.model.response.AccessTokenModel
import com.startwithn.exchange_android.model.response.ExchangeCalculateResponse
import com.startwithn.exchange_android.model.response.MessageModel
import com.startwithn.exchange_android.model.response.RegisterResponseModel
import com.startwithn.exchange_android.model.response.TopUpResponse
import com.startwithn.exchange_android.model.response.TransactionsModel
import com.startwithn.exchange_android.model.response.UploadResponseModel
import com.startwithn.exchange_android.model.response.UserModel
import com.startwithn.exchange_android.network.AppAPI
import com.startwithn.exchange_android.network.ResultWrapper
import com.startwithn.exchange_android.repository.remote.base.BaseRemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody

class AppRemoteRepository(
    private val context: Context,
    private val api: AppAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRemoteRepository(context) {

    suspend fun getLastTransactions(): ResultWrapper<BaseResponseModel<List<TransactionsModel>>> {
        return safeApiCall(dispatcher, call = { api.lastTransactions(1, 10) })
    }

    suspend fun getHistoryTransactions(page: Int, limit: Int, from: String, to: String): ResultWrapper<BaseResponseModel<List<TransactionsModel>>> {
        return safeApiCall(dispatcher, call = {
            api.historyTransactions(
                page = page, limit = limit, dateFrom = from, dateTo = to
            )
        })
    }

    suspend fun topUp(amount: Double): ResultWrapper<TopUpResponse> {
        return safeApiCall(dispatcher, call = {
            api.topUp(value = amount)
        })
    }

    suspend fun exchange(requestModel: ExchangeRequestModel): ResultWrapper<TopUpResponse> {
        return safeApiCall(dispatcher, call = {
            api.exchange(
                currencyFromID = requestModel.currencyFormId,
                currencyToID = requestModel.currencyToId,
                amount = requestModel.amount
            )
        })
    }

    suspend fun exchangeCalculate(requestModel: ExchangeRequestModel): ResultWrapper<ExchangeCalculateResponse> {
        return safeApiCall(dispatcher, call = {
            api.exchangeCalculate(
                currencyFromID = requestModel.currencyFormId,
                currencyToID = requestModel.currencyToId,
                amount = requestModel.amount
            )
        })
    }

}
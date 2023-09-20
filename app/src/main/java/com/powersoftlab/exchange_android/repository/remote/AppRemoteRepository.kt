package com.powersoftlab.exchange_android.repository.remote

import android.content.Context
import com.powersoftlab.exchange_android.model.base.BaseResponseModel
import com.powersoftlab.exchange_android.model.body.ExchangeRequestModel
import com.powersoftlab.exchange_android.model.body.RequestNewCardRequestModel
import com.powersoftlab.exchange_android.model.response.AddressAutoFillResponseModel
import com.powersoftlab.exchange_android.model.response.CardsResponseModel
import com.powersoftlab.exchange_android.model.response.ExchangeCalculateResponse
import com.powersoftlab.exchange_android.model.response.RequestNewCardResponseModel
import com.powersoftlab.exchange_android.model.response.TopUpResponse
import com.powersoftlab.exchange_android.model.response.TransactionsModel
import com.powersoftlab.exchange_android.network.AppAPI
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.base.BaseRemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

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

    suspend fun getSubDistricts(): ResultWrapper<BaseResponseModel<List<AddressAutoFillResponseModel.SubDistrictResponse>>> {
        return safeApiCall(dispatcher, call = {
            api.subDistricts()
        })
    }

    suspend fun getAddressDataBySubId(id : Int): ResultWrapper<BaseResponseModel<AddressAutoFillResponseModel>> {
        return safeApiCall(dispatcher, call = {
            api.getAddressBySubDistrictId(id)
        })
    }

    suspend fun getCards(): ResultWrapper<List<CardsResponseModel>> {
        return safeApiCall(dispatcher, call = {
            api.getCards()
        })
    }

    suspend fun requestNewCard(
        request: RequestNewCardRequestModel
    ): ResultWrapper<RequestNewCardResponseModel>{
        return safeApiCall(
            dispatcher,
            call = {
                api.registerNewCard(request)
            })
    }

}
package com.powersoftlab.exchange_android.ui.page.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linecorp.linesdk.LineProfile
import com.linecorp.linesdk.Scope
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginApi
import com.linecorp.linesdk.auth.LineLoginResult
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.common.other.SingleLiveEvent
import com.powersoftlab.exchange_android.model.base.BaseResponseModel
import com.powersoftlab.exchange_android.model.body.LoginRequestModel
import com.powersoftlab.exchange_android.model.response.AccessTokenModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.repository.remote.UserRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class LoginViewModel(private val userRemoteRepository: UserRemoteRepository,
                     private val appManager: AppManager
) : ViewModel() {

    val loginLiveData = SingleLiveEvent<ResultWrapper<BaseResponseModel<AccessTokenModel>>>()
    val iconLeftMenu = MutableLiveData<Int>()

    private val _userProfileFlow = MutableStateFlow<LineProfile?>(null)
    val userProfileFlow
        get(): StateFlow<LineProfile?> = _userProfileFlow

    fun login(request: LoginRequestModel) {
        viewModelScope.launch {
            loginLiveData.value = ResultWrapper.Loading
            val result = userRemoteRepository.login(request)
            if(result is ResultWrapper.Success){
                result.response.data?.let { token ->
                    appManager.setAuthToken(token.accessToken)
                }
            }
            loginLiveData.value = result
        }
    }

    fun setIcon(iconRes : Int){
        iconLeftMenu.value = iconRes
    }

    //region line login
    fun createLineLoginIntent(
        context: Context,
        channelId: String,
        scopes: List<Scope> = emptyList(),
        nonce: String? = null,
        botPrompt: LineAuthenticationParams.BotPrompt? = null,
        uiLocale: Locale? = null,
        onlyWebLogin: Boolean = false
    ): Intent {
        val loginAuthParam = createLoginAuthParam(scopes, nonce, botPrompt, uiLocale)

        return if (onlyWebLogin) {
            LineLoginApi.getLoginIntentWithoutLineAppAuth(
                context,
                channelId,
                loginAuthParam
            )
        } else {
            LineLoginApi.getLoginIntent(
                context,
                channelId,
                loginAuthParam
            )
        }
    }
    private fun createLoginAuthParam(
        scopes: List<Scope>,
        nonce: String?,
        botPrompt: LineAuthenticationParams.BotPrompt?,
        uiLocale: Locale?
    ): LineAuthenticationParams = LineAuthenticationParams.Builder()
        .scopes(scopes)
        .nonce(nonce)
        .uiLocale(uiLocale)
        .botPrompt(botPrompt)
        .build()

    fun processLoginIntent(resultCode: Int, intent: Intent?) {
//        if (!isResultCodeOk(resultCode)) {
//            val errorMessage = intent?.dataString ?: "login error but no error message"
//            processFailureMsg(errorMessage)
//            return
//        }
//
//        if (intent == null) {
//            processFailureMsg("success but no intent")
//            return
//        }

        val loginResult = LineLoginApi.getLoginResultFromIntent(intent)
        processLoginResult(loginResult)
    }
    fun processLoginResult(result: LineLoginResult) = with(result) {
//        if (!isSuccess) {
//            processFailureMsg(responseCode.name, errorData.message)
//            return
//        }
//
//        if (lineProfile == null) {
//            processFailureMsg("lineProfile of LineLoginResult is null.", errorData.message)
//            return
//        }

        //_isLoginFlow.update { true }
        _userProfileFlow.update { lineProfile }
    }

    //endregion






}
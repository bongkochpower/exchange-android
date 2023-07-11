package com.startwithn.exchange_android.ui.page.main//package com.feyverly.hipowershot.ui.page.main.activity
//
//import android.content.Context
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.startwithn.exchange_android.common.manager.AppManager
//import com.feyverly.hipowershot.model.*
//import com.startwithn.exchange_android.network.ResultWrapper
//import com.feyverly.hipowershot.repository.remote.ProductRemoteRepository
//import com.feyverly.hipowershot.repository.remote.ProfileRemoteRepository
//import kotlinx.coroutines.launch
//import timber.log.Timber
//
//class MainViewModel(
//    private val context: Context,
//    private val appManager: AppManager,
//    private val profileRemoteRepository: ProfileRemoteRepository,
//    private val productRemoteRepository: ProductRemoteRepository,
//) : ViewModel() {
//
//    val isRequiredLogin: MutableLiveData<Boolean> = MutableLiveData()
//
//    private val me: MutableLiveData<ResultWrapper<DataModel<UserModel>>> =
//        MutableLiveData()
//
//    val userModel: MutableLiveData<UserModel> = MutableLiveData()
//
//    private val toggleNotification: MutableLiveData<ResultWrapper<DataModel<MessageModel>>> =
//        MutableLiveData()
//
//    val isOpenNotification: MutableLiveData<Boolean> = MutableLiveData()
//
//    private val flashSales: MutableLiveData<ResultWrapper<DataModel<ProductModel>>> =
//        MutableLiveData()
//
//    init {
//        isOpenNotification.value = appManager.isOpenNotification()
//    }
//
//    fun refreshSession() {
//        val result = appManager.getAuthToken().isNullOrEmpty()
//        isRequiredLogin.value = result
//        if (!result) {
//            getMe()
//        }
//    }
//
//    fun getMe() {
//        me.value = ResultWrapper.Loading
//        viewModelScope.launch {
//            val result = profileRemoteRepository.me()
//            if (result is ResultWrapper.Success) {
//                Timber.d("me -> $result")
//                result.data.let {
//                    it.data?.let { _userModel ->
//                        /*update default address*/
//                        val defaultAddressModel: AddressModel? =
//                            _userModel.addresses?.find { addressModel -> addressModel.isDefault == true }
//                        AppManager(context).setAddress(defaultAddressModel)
//                        /*update notification*/
//                        AppManager(context).setOpenNotification(_userModel.isNotify == true)
//                        userModel.value = _userModel
//                    }
//                }
//            }
//            me.value = result
//        }
//    }
//
//    fun meResult(): MutableLiveData<ResultWrapper<DataModel<UserModel>>> = me
//
//    fun toggleNotification(isOpen: Boolean) {
//        toggleNotification.value = ResultWrapper.Loading
//        viewModelScope.launch {
//            toggleNotification.value = profileRemoteRepository.toggleNotification()
//        }
//        isOpenNotification.value = appManager.setOpenNotification(isOpen)
//    }
//
//    fun getFlashSales() {
//        flashSales.value = ResultWrapper.Loading
//        viewModelScope.launch {
//            flashSales.value = productRemoteRepository.flashSales()
//        }
//    }
//
//    fun flashSalesResult(): MutableLiveData<ResultWrapper<DataModel<ProductModel>>> = flashSales
//
//}
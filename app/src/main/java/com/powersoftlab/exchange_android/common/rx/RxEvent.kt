package com.powersoftlab.exchange_android.common.rx

import com.powersoftlab.exchange_android.common.enum.AppEventEnum

class RxEvent {
    data class AppEvent(val event: AppEventEnum, val message: String? = null)
}
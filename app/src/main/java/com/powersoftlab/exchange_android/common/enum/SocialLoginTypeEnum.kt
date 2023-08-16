package com.powersoftlab.exchange_android.common.enum

import com.powersoftlab.exchange_android.R

enum class SocialLoginTypeEnum() {
    FB,
    LINE,
    APP;


    companion object {
        fun fromName(name: String?): SocialLoginTypeEnum {
            if (name != null) {
                values().find { name.equals(it.name, true) }?.let {
                    return it
                }
            }
            return FB
        }
    }
}
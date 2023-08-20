package com.powersoftlab.exchange_android.common.enum

enum class LoginTypeEnum() {
    FB,
    LINE,
    APP;


    companion object {
        fun fromName(name: String?): LoginTypeEnum {
            if (name != null) {
                values().find { name.equals(it.name, true) }?.let {
                    return it
                }
            }
            return FB
        }
    }
}
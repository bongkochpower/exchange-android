package com.powersoftlab.exchange_android.common.enum

enum class AppEventEnum(val code: String) {

    FORCE_UPDATE("force"),
    SOFT_UPDATE("soft"),
    MAINTENANCE("maintenance"),
    UNAUTHORIZED("unauthorized"),
    NOTIFY_UPDATE("notify"),
    NONE("none");

    companion object {
        fun fromCode(code: String?): AppEventEnum {
            if (code != null) {
                values().find { code.equals(it.code, true) }?.let {
                    return it
                }
            }
            return NONE
        }
    }
}
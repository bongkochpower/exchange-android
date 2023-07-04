package com.startwithn.exchange_android.ext

fun String.isPhone(): Boolean = android.util.Patterns.PHONE.matcher(this).matches()

fun String.isEmail(): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
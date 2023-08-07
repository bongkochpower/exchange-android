package com.powersoftlab.exchange_android.ext

fun String.isPhone(): Boolean = android.util.Patterns.PHONE.matcher(this).matches()

fun String.isEmail(): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

internal fun isValidPassword(password: String): Boolean {
    if (password.length < 8) return false
    if (password.filter { it.isDigit() }.firstOrNull() == null) return false
    if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
    if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
    if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false

    return true
}
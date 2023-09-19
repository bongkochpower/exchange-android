package com.powersoftlab.exchange_android.ext

import android.text.Editable
import android.text.TextWatcher
import com.powersoftlab.exchange_android.ui.widget.EdittextRegister

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

fun EdittextRegister.validateAfterTextChange() {
    this.addOnTextChangeListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            if (p0?.toString().orEmpty().isNotEmpty() && this@validateAfterTextChange.getValidation()) {
                this@validateAfterTextChange.setValidation(false)
            }
        }
    })
}
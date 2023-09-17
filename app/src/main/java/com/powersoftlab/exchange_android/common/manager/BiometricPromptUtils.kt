package com.powersoftlab.exchange_android.common.manager

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.powersoftlab.exchange_android.R
import timber.log.Timber

object BiometricPromptUtils {

    const val TAG = "BiometricPromptUtils"

    inline fun createBiometricPrompt(
        activity: AppCompatActivity,
        crossinline onSuccess: (result: androidx.biometric.BiometricPrompt.AuthenticationResult) -> Unit,
        crossinline onError: (errorCode: Int, message: String) -> Unit
    ): androidx.biometric.BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errCode, errString)
                Timber.e("$TAG -> errCode is $errCode and errString is: $errString")
                onError(errCode, errString.toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Timber.e("$TAG -> User biometric rejected.")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Timber.d("$TAG -> Authentication was successful")
                onSuccess(result)
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

    fun createPromptInfo(context: Context): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().apply {
            setTitle("${context.getString(R.string.prompt_info_title)} ${context.getString(R.string.app_name)}")
            setSubtitle(context.getString(R.string.prompt_info_subtitle))
//            setDescription(context.getString(R.string.prompt_info_description))
            setNegativeButtonText(context.getString(R.string.prompt_info_negative_button))
        }.build()

    fun isCanAuthenticate(context: Context): Boolean =
        BiometricManager.from(context)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
}
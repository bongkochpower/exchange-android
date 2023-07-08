package com.startwithn.exchange_android.common.alert

import android.content.Context
import com.startwithn.exchange_android.BuildConfig
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.common.manager.AppManager

class AppAlert {
    companion object {
        fun alertNetworkError(
            context: Context,
            callBack: (() -> Unit)? = null
        ): AlertDialogFragment {
            val positiveButton = context.getString(R.string.button_close
            )
            val dialog = AlertDialogFragment.newInstance(
                title = context.getString(R.string.message_no_internet),
                message = context.getString(R.string.message_please_check_no_internet),
                confirmButton = positiveButton
            )
            dialog.setPositiveButton {
                callBack?.invoke()
            }
            return dialog
        }

        fun alertGenericError(
            context: Context,
            code: Int?,
            message: String?,
            callBack: (() -> Unit)? = null
        ): AlertDialogFragment {
            val positiveButton = context.getString(R.string.button_close)
            return if (BuildConfig.DEBUG) {
                val dialog = AlertDialogFragment.newInstance(
                    title = context.resources?.getString(R.string.title_code) + " " + code.toString(),
                    message = message,
                    confirmButton = positiveButton
                )
                dialog.setPositiveButton {
                    callBack?.invoke()
                }
                dialog
            } else {
                val dialog = AlertDialogFragment.newInstance(
                    message = message,
                    confirmButton = positiveButton
                )
                dialog.setPositiveButton {
                    callBack?.invoke()
                }
                dialog
            }
        }

        fun alert(context: Context, message: String?): AlertDialogFragment {
            return alert(context, null, message)
        }

        fun alert(context: Context, title: String?, message: String?): AlertDialogFragment {
            val positiveButton = context.getString(R.string.button_close)
            val dialog = AlertDialogFragment.newInstance(
                title = title,
                message = message,
                confirmButton = positiveButton
            )
            dialog.setPositiveButton {

            }
            return dialog
        }


        fun confirm(
            context: Context,
            message: String?,
            confirmButton: String?,
            confirmCallBack: () -> Unit,
            cancelable: Boolean = true
        ): AlertDialogFragment {
            val dialog = AlertDialogFragment.newInstance(
                message = message,
                confirmButton = confirmButton,
                cancelable = cancelable
            )
            dialog.setPositiveButton {
                confirmCallBack.invoke()
            }
            return dialog
        }

        fun confirm(
            context: Context,
            message: String?,
            confirmButton: String?,
            confirmCallBack: () -> Unit,
            cancelButton: String?,
            cancelCallBack: () -> Unit,
            cancelable: Boolean = true
        ): AlertDialogFragment {
            val dialog = AlertDialogFragment.newInstance(
                message = message,
                confirmButton = confirmButton,
                cancelButton = cancelButton,
                cancelable = cancelable
            )
            dialog.setNegativeButton {
                cancelCallBack.invoke()
            }
            dialog.setPositiveButton {
                confirmCallBack.invoke()
            }
            return dialog
        }

        fun confirm(
            context: Context,
            overrideThemeRes: Int? = null,
            message: String?,
            confirmButton: String?,
            confirmCallBack: () -> Unit,
            cancelButton: String?,
            cancelCallBack: () -> Unit,
            cancelable: Boolean = true
        ): AlertDialogFragment {

            val dialog = AlertDialogFragment.newInstance(
                overrideThemeRes = overrideThemeRes,
                message = message,
                confirmButton = confirmButton,
                cancelButton = cancelButton,
                cancelable = cancelable
            )
            dialog.setNegativeButton {
                cancelCallBack.invoke()
            }
            dialog.setPositiveButton {
                confirmCallBack.invoke()
            }
            return dialog
        }

    }
}
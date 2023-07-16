package com.startwithn.exchange_android.common.alert

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AlertDialogFragment : DialogFragment() {
    private lateinit var dialog: MaterialAlertDialogBuilder
    private var callbackPositiveButton: ((Unit) -> Unit)? = null
    private var callbackNegativeButton: ((Unit) -> Unit)? = null

    companion object {
        private const val TAG = "dialog_alert_fragment"

        private const val KEY_TITLE = "key_title"
        private const val KEY_THEME_RES = "key_theme_res"
        private const val KEY_MESSAGE = "key_message"
        private const val KEY_CONFIRM_BUTTON = "key_confirm_button"
        private const val KEY_CANCEL_BUTTON = "key_cancel_button"
        private const val KEY_CANCELABLE = "key_cancelable"

        fun newInstance(
            title: String? = null,
            overrideThemeRes: Int? = null,
            message: String? = null,
            confirmButton: String? = null,
            cancelButton: String? = null,
            cancelable: Boolean = true
        ): AlertDialogFragment {
            val fragment = AlertDialogFragment()
            val bundle = Bundle()
            title?.let {
                bundle.putString(KEY_TITLE, it)
            }
            overrideThemeRes?.let {
                bundle.putInt(KEY_THEME_RES, it)
            }
            message?.let {
                bundle.putString(KEY_MESSAGE, it)
            }
            confirmButton?.let {
                bundle.putString(KEY_CONFIRM_BUTTON, it)
            }
            cancelButton?.let {
                bundle.putString(KEY_CANCEL_BUTTON, it)
            }
            bundle.putBoolean(KEY_CANCELABLE, cancelable)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val overrideThemeRes = arguments?.getInt(KEY_THEME_RES)
        val title = arguments?.getString(KEY_TITLE)
        val message = arguments?.getString(KEY_MESSAGE)
        val confirmButton = arguments?.getString(KEY_CONFIRM_BUTTON)
        val cancelButton = arguments?.getString(KEY_CANCEL_BUTTON)
        val cancelable = arguments?.getBoolean(KEY_CANCELABLE) ?: true

        isCancelable = cancelable

        return try {
            dialog = if (overrideThemeRes != null) {
                MaterialAlertDialogBuilder(requireContext(), overrideThemeRes)
            } else {
                MaterialAlertDialogBuilder(requireContext())
            }
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setPositiveButton(
                confirmButton
            ) { dialog, _ ->
                callbackPositiveButton?.invoke(Unit)
                dialog.dismiss()
            }
            dialog.setNegativeButton(
                cancelButton
            ) { dialog, _ ->
                callbackNegativeButton?.invoke(Unit)
                dialog.dismiss()
            }
            dialog.create()
        } catch (e: IllegalStateException) {
            super.onCreateDialog(savedInstanceState)
        }
    }


    fun setPositiveButton(callbackPositiveButton: ((Unit) -> Unit)) {
        this.callbackPositiveButton = callbackPositiveButton
    }

    fun setNegativeButton(callbackNegativeButton: ((Unit) -> Unit)) {
        this.callbackNegativeButton = callbackNegativeButton
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }


}
package com.startwithn.exchange_android.ui.dialog.popup

import android.content.DialogInterface
import android.os.Bundle
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.DialogAlertSuccessBinding
import com.startwithn.exchange_android.ui.dialog.popup.base.BaseDialogFragment

class AlertSuccessDialog :
    BaseDialogFragment<DialogAlertSuccessBinding>(
        layout = R.layout.dialog_alert_success,
        isCanBack = false,
        isAnimation = true
    ) {

    private var onConfirmClick: ((AlertSuccessDialog) -> Unit)? = null
    private var onDismissListener: (() -> Unit)? = null

    private var title: String? = null
        set(value) {
            binding.txtMessage.isVisible = !value.isNullOrEmpty()
            binding.txtMessage.text = HtmlCompat.fromHtml(
                value.toString().replace("\n","<br>"),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            field = value
        }
    private var textButtonRight: String? = null
        set(value) {
            binding.btnConfirm.text = value
            field = value
        }

    companion object {
        private const val KEY_TITLE = "key_title"
        private const val KEY_MESSAGE = "key_message"
        private const val KEY_TEXT_BUTTON_LEFT = "key_text_button_left"
        private const val KEY_TEXT_BUTTON_RIGHT = "key_text_button_right"
        private const val KEY_IS_SINGLE_CHOICE = "key_is_single_choice"
        private const val KEY_IS_MESSAGE_ALIGN_LEFT = "key_is_message_align_left"

        fun newInstance(
            title: String? = null,
            textButtonRight: String? = null,
        ) = AlertSuccessDialog().apply {
            arguments = Bundle().apply {
                putString(KEY_TITLE, title)
                putString(KEY_TEXT_BUTTON_RIGHT, textButtonRight)
            }
        }
    }

    override fun getExtra(bundle: Bundle) {
        super.getExtra(bundle)
        title = bundle.getString(KEY_TITLE,getString(R.string.message_register_success))
        textButtonRight = bundle.getString(KEY_TEXT_BUTTON_RIGHT, getString(R.string.button_start_working))
    }

    override fun setUp() {
        with(binding) {
            btnConfirm.setOnClickListener { onConfirmClick?.invoke(this@AlertSuccessDialog) }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissListener?.invoke()
        super.onDismiss(dialog)
    }

    fun setOnButtonConfirmClick(onRightClick: ((AlertSuccessDialog) -> Unit)?) {
        this.onConfirmClick = onRightClick
    }

    fun setOnDismissListener(onDismissListener: (() -> Unit)?) {
        this.onDismissListener = onDismissListener
    }
}
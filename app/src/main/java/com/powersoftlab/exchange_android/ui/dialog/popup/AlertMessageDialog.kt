package com.powersoftlab.exchange_android.ui.dialog.popup

import android.content.DialogInterface
import android.os.Bundle
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.DialogAlertMessageBinding
import com.powersoftlab.exchange_android.ui.dialog.popup.base.BaseDialogFragment

class AlertMessageDialog :
    BaseDialogFragment<DialogAlertMessageBinding>(
        layout = R.layout.dialog_alert_message,
        isCanBack = true,
        isAnimation = true
    ) {

    private var onLeftClick: ((AlertMessageDialog) -> Unit)? = null
    private var onRightClick: ((AlertMessageDialog) -> Unit)? = null
    private var onDismissListener: (() -> Unit)? = null

    private var title: String? = null
        set(value) {
            binding.tvTitle.isVisible = !value.isNullOrEmpty()
            binding.tvTitle.text = HtmlCompat.fromHtml(
                value.toString().replace("\n","<br>"),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            field = value
        }
    private var message: String? = null
        set(value) {
            binding.tvMessage.text = HtmlCompat.fromHtml(
                value.toString().replace("\n","<br>"),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            field = value
        }
    private var textButtonLeft: String? = null
        set(value) {
            binding.btnLeft.text = value
            field = value
        }
    private var textButtonRight: String? = null
        set(value) {
            binding.btnRight.text = value
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
            message: String? = null,
            textButtonLeft: String? = null,
            textButtonRight: String? = null,
            isSingleChoice: Boolean = false,
            isMessageLeft: Boolean = false,
        ) = AlertMessageDialog().apply {
            arguments = Bundle().apply {
                putString(KEY_TITLE, title)
                putString(KEY_MESSAGE, message)
                putString(KEY_TEXT_BUTTON_LEFT, textButtonLeft)
                putString(KEY_TEXT_BUTTON_RIGHT, textButtonRight)
                putBoolean(KEY_IS_SINGLE_CHOICE, isSingleChoice)
                putBoolean(KEY_IS_MESSAGE_ALIGN_LEFT, isMessageLeft)
            }
        }
    }

    override fun getExtra(bundle: Bundle) {
        super.getExtra(bundle)
        title = bundle.getString(KEY_TITLE)
        message = bundle.getString(KEY_MESSAGE)
        textButtonLeft = bundle.getString(KEY_TEXT_BUTTON_LEFT, getString(R.string.button_cancel))
        textButtonRight = bundle.getString(KEY_TEXT_BUTTON_RIGHT, getString(R.string.button_confirm))
        binding.isSingleChoice = bundle.getBoolean(KEY_IS_SINGLE_CHOICE)
        binding.isMessageAlignLeft = bundle.getBoolean(KEY_IS_MESSAGE_ALIGN_LEFT)
    }

    override fun setUp() {
        with(binding) {
            btnLeft.setOnClickListener { onLeftClick?.invoke(this@AlertMessageDialog) }
            btnRight.setOnClickListener { onRightClick?.invoke(this@AlertMessageDialog) }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissListener?.invoke()
        super.onDismiss(dialog)
    }

    fun setOnButtonLeftClick(onLeftClick: ((AlertMessageDialog) -> Unit)?) {
        this.onLeftClick = onLeftClick
    }

    fun setOnButtonRightClick(onRightClick: ((AlertMessageDialog) -> Unit)?) {
        this.onRightClick = onRightClick
    }

    fun setOnDismissListener(onDismissListener: (() -> Unit)?) {
        this.onDismissListener = onDismissListener
    }
}
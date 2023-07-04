package com.startwithn.exchange_android.ui.dialog.popup

import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.DialogProgressBinding
import com.startwithn.exchange_android.ui.dialog.popup.base.BaseDialogFragment

class ProgressDialog : BaseDialogFragment<DialogProgressBinding>(R.layout.dialog_progress) {

    companion object {
        @JvmStatic
        fun newInstance() = ProgressDialog()
    }

    override fun setUp() {}

}
package com.powersoftlab.exchange_android.ui.dialog.popup

import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.DialogProgressBinding
import com.powersoftlab.exchange_android.ui.dialog.popup.base.BaseDialogFragment

class ProgressDialog : BaseDialogFragment<DialogProgressBinding>(R.layout.dialog_progress) {

    companion object {
        @JvmStatic
        fun newInstance() = ProgressDialog()
    }

    override fun setUp() {}

}
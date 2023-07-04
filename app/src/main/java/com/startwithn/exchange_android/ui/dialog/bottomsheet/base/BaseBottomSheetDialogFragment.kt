package com.startwithn.exchange_android.ui.dialog.bottomsheet.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.startwithn.exchange_android.ext.fixFontScale
import com.startwithn.exchange_android.ext.hideKeyboard
import com.startwithn.exchange_android.ui.dialog.popup.ProgressDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.startwithn.exchange_android.R

abstract class BaseBottomSheetDialogFragment<B : ViewDataBinding> : BottomSheetDialogFragment() {

    protected lateinit var binding: B
    var isShow: Boolean = false
    protected val progressDialog: ProgressDialog by lazy { ProgressDialog.newInstance() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        //dialog.setWhiteNavigationBar()
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            getExtra(it)
        }

        context?.apply {
            fixFontScale()
            //overrideUIText(binding.root)
        }

        setUp()

        subscribe()

        hideKeyboard()
    }

    abstract fun getLayoutId(): Int

    open fun getExtra(bundle: Bundle) {}

    open fun setUp() {}

    open fun subscribe() {}

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        isShow = true
    }

    override fun dismiss() {
        try {
            if (isShow) {
                super.dismiss()
                isShow = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
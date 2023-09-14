package com.powersoftlab.exchange_android.ui.dialog.bottomsheet.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleObserver
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.ext.fixFontScale
import com.powersoftlab.exchange_android.ext.hideKeyboard
import com.powersoftlab.exchange_android.ui.dialog.popup.ProgressDialog

abstract class BaseBottomSheetDialogFragment<B : ViewDataBinding>(@LayoutRes private val layout: Int) :
    BottomSheetDialogFragment(),
    LifecycleObserver {

    protected lateinit var binding: B

    protected val TAG = javaClass.name

    var isShow: Boolean = false

    protected val progressDialog: ProgressDialog by lazy { ProgressDialog.newInstance() }

    companion object {
        private const val EXTRA_IS_SHOW = "extra_is_show"
    }

    override fun onPause() {
        super.onPause()
        // Disable dialog window animations for this instance
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            dialog?.window?.setWindowAnimations(-1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.LightBottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_DRAGGING
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        lifecycle.addObserver(this)

        arguments?.let {
            getExtra(it)
        }

        context?.apply {
            fixFontScale()
//            overrideUIText(binding.root)
        }

        setUp()

        subscribe()

        hideKeyboard()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isAdded) {
            isShow = true
            super.show(manager, tag)
        }
    }

    override fun dismiss() {
        try {
            isShow = false
            super.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun getExtra(bundle: Bundle) {
        with(bundle) {
            isShow = getBoolean(EXTRA_IS_SHOW, true)
        }
    }

    abstract fun setUp()

    open fun subscribe() {}

    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//    private fun dismissBottomSheet() {
//        dismiss()
//    }
}
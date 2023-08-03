package com.powersoftlab.exchange_android.ui.dialog.popup.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.ext.fixFontScale
import com.powersoftlab.exchange_android.ext.hideKeyboard
import com.powersoftlab.exchange_android.ui.dialog.popup.ProgressDialog

abstract class BaseDialogFragment<B : ViewDataBinding>(
    @LayoutRes private val layout: Int,
    private var isCanBack: Boolean = false,
    private var isAnimation: Boolean = false,
    private var animationStyle: Int = R.style.DialogSlideAnimation
) : DialogFragment() {

    protected val TAG = javaClass.name

    protected lateinit var binding: B

    var isShow: Boolean = false

    protected val progressDialog: ProgressDialog by lazy { ProgressDialog.newInstance() }

    companion object {
        private const val EXTRA_IS_SHOW = "extra_is_show"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (isAnimation) {
                attributes?.windowAnimations = animationStyle
            }
        }
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun onStart() {
        super.onStart()

        dialog.let {
            it?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            it?.setCancelable(isCanBack)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putBoolean(EXTRA_IS_SHOW, isShow)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if(!isAdded) {
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

    //    abstract fun getLayoutId(): Int

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
}
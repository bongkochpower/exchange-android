package com.feyverly.hipowershot.ui.page.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.ext.fixFontScale
import com.startwithn.exchange_android.ext.hideKeyboard
import com.startwithn.exchange_android.ui.dialog.popup.ProgressDialog
import org.koin.android.ext.android.inject

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    protected val appManager: AppManager by inject()

    protected lateinit var binding: B
    protected val progressDialog: ProgressDialog by lazy { ProgressDialog.newInstance() }

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

}
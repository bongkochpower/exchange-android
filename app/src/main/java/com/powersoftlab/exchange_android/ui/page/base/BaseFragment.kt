package com.powersoftlab.exchange_android.ui.page.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.powersoftlab.exchange_android.common.manager.BiometricPromptUtils
import com.powersoftlab.exchange_android.ext.fixFontScale
import com.powersoftlab.exchange_android.ext.hideKeyboard
import com.powersoftlab.exchange_android.ui.dialog.popup.AlertSuccessDialog
import com.powersoftlab.exchange_android.ui.dialog.popup.ProgressDialog

abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes private val layout: Int) : Fragment() {

    protected val TAG = javaClass.name

    protected lateinit var binding: B

    protected val progressDialog: ProgressDialog by lazy { ProgressDialog.newInstance() }

    private lateinit var biometricPrompt: BiometricPrompt

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

        arguments?.let {
            getExtra(it)
        }

        context?.apply {
            fixFontScale()
//            overrideUIText(binding.root)
        }


        setUp()
        listener()

        subscribe()

        hideKeyboard()
    }

    open fun getExtra(bundle: Bundle){}

    abstract fun setUp()
    abstract fun listener()

    open fun subscribe() {}

    fun showLoading(){
        if(progressDialog.isShow) return
        progressDialog.show(childFragmentManager)
    }
    fun hideLoading(){
        progressDialog.dismissAllowingStateLoss()
    }

    fun showAlertSuccessDialog(
        title: String? = null,
        textButtonRight: String? = null,
        onRightClick: (() -> Unit)? = null
    ) {
        var alertSuccessDialog: AlertSuccessDialog? = null
        alertSuccessDialog = AlertSuccessDialog.newInstance(
            title = title,
            textButtonRight = textButtonRight
        )
        alertSuccessDialog.apply {
            setOnButtonConfirmClick {
                onRightClick?.invoke()
                it.dismiss()
            }

        }
        alertSuccessDialog.show(requireActivity().supportFragmentManager)
    }

    fun showBiometricPrompt(
        onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (errorCode: Int, message: String) -> Unit
    ) {
        biometricPrompt =
            BiometricPromptUtils.createBiometricPrompt(
                this.activity as AppCompatActivity,
                onSuccess,
                onError
            )
        biometricPrompt.authenticate(BiometricPromptUtils.createPromptInfo(this.requireContext()))
    }

}
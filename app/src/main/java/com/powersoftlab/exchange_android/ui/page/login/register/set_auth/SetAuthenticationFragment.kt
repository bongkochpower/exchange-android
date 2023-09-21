package com.powersoftlab.exchange_android.ui.page.login.register.set_auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.poovam.pinedittextfield.PinField
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.common.manager.BiometricPromptUtils
import com.powersoftlab.exchange_android.common.navigator.AppNavigator
import com.powersoftlab.exchange_android.databinding.FragmentSetAuthBinding
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.login.LoginViewModel
import com.powersoftlab.exchange_android.ui.page.main.withdraw.auth_with_bio.AuthWithBioViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class SetAuthenticationFragment : BaseFragment<FragmentSetAuthBinding>(R.layout.fragment_set_auth), OnBackPressedFragment {

    private val loginViewModel: LoginViewModel by sharedViewModel()
    private val authViewModel: AuthWithBioViewModel by stateViewModel()
    private var mPin: String? = null
    //private val appManager: AppManager by inject()

    companion object {
        fun newInstance() = SetAuthenticationFragment()

        fun navigate(fragment: Fragment, action: NavDirections) =
            fragment.findNavController().navigate(action)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            fadeIn()
        }
    }

    override fun setUp() {
        loginViewModel.setIcon(0)
        with(binding) {
            lifecycleOwner = this.lifecycleOwner
            isPinStep = true
        }
    }

    override fun listener() {
        with(binding) {

            layoutSetPin.apply {
                tvResetPin.setOnClickListener {
                    spfOtp.text?.clear()
                }
                btnConfirmPin.apply {
                    setOnTouchAnimation()
                    setOnClickListener {
                        mPin?.let {
                            setPin(it)
                        }
                    }
                }

                spfOtp.onTextCompleteListener = object : PinField.OnTextCompleteListener {
                    override fun onTextComplete(enteredText: String): Boolean {
                        isEnableBtnConfirm = true
                        mPin = enteredText
                        return true
                    }
                }
            }

            layoutSetFinger.apply {
                imgFingerScan.apply {
                    setOnTouchAnimation()
                    setOnClickListener {
                        showBiometricPrompt(
                            { result ->
                                setFingerScanSuccess()
                            },
                            { _, message ->
                                Toast.makeText(requireContext(), "${message}", Toast.LENGTH_SHORT).show()
                            })
                    }
                }
            }

        }
    }

    override fun subscribe() {
        super.subscribe()

        authViewModel.getAuthSecretKeyLiveData.observe(viewLifecycleOwner){
            when (it) {
                is ResultWrapper.Loading -> {
                    showLoading()
                }

                is ResultWrapper.Success -> {
                    authViewModel.createPin()
                }

                is ResultWrapper.GenericError -> {
                    hideLoading()
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                is ResultWrapper.NetworkError -> {
                    hideLoading()
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                else -> {
                    hideLoading()
                }
            }
        }
        authViewModel.createPinLiveData.observe(viewLifecycleOwner){
            when (it) {
                is ResultWrapper.Loading -> {
                }

                is ResultWrapper.Success -> {
                    hideLoading()
                    setPinSuccess()
                }

                is ResultWrapper.GenericError -> {
                    hideLoading()
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                is ResultWrapper.NetworkError -> {
                    hideLoading()
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                else -> {
                    hideLoading()
                }
            }
        }
    }


    private fun fadeIn() {
        with(binding) {
        }
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    private fun setPin(pin: String) {
            authViewModel.pin = pin
            authViewModel.getAuthSecretKey()
    }

    private fun setPinSuccess() {
        val msg = resources.getString(R.string.message_set_pin_success)
        val btn = resources.getString(R.string.button_next)
        showAlertSuccessDialog(title = msg, textButtonRight = btn) {
            if (BiometricPromptUtils.isCanAuthenticate(requireContext())) {
                gotoSetFingerScan()
            } else {
                AppNavigator(this.requireActivity()).goToLogin(true)
            }
        }
    }
    private fun setFingerScanSuccess() {
        val msg = resources.getString(R.string.message_set_finger_scan_success)
        val btn = resources.getString(R.string.button_next)
        showAlertSuccessDialog(title = msg, textButtonRight = btn) {
            AppNavigator(this.requireActivity()).goToLogin(true)
        }
    }

    private fun gotoSetFingerScan() {
        binding.apply {
            isPinStep = false
        }
    }

}
package com.powersoftlab.exchange_android.ui.page.main.withdraw.auth_with_bio

import android.util.Log
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.poovam.pinedittextfield.PinField
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.common.constant.AppConstant
import com.powersoftlab.exchange_android.common.enum.AuthByEnum
import com.powersoftlab.exchange_android.common.navigator.AppNavigator
import com.powersoftlab.exchange_android.databinding.FragmentAuthWithBioBinding
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.model.response.WithdrawResponseModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.exchange.ExchangeViewModel
import com.powersoftlab.exchange_android.ui.page.main.topup.TopUpViewModel
import com.powersoftlab.exchange_android.ui.page.main.withdraw.WithdrawViewModel
import com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_summary.WithDrawSummaryFragment
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class AuthWithBioFragment : BaseFragment<FragmentAuthWithBioBinding>(R.layout.fragment_auth_with_bio), OnBackPressedFragment {

    private val authViewModel: AuthWithBioViewModel by stateViewModel()
    private val topupViewModel: TopUpViewModel by stateViewModel()
    private val exchangeViewModel: ExchangeViewModel by sharedStateViewModel()
    private val withdrawViewModel: WithdrawViewModel by sharedStateViewModel()
    private val args: AuthWithBioFragmentArgs by navArgs()


    companion object {
        fun newInstance() = AuthWithBioFragment()
        fun navigate(fragment: Fragment, navDirections: NavDirections) =
            fragment.findNavController().navigate(navDirections)

    }

    override fun setUp() {
        binding.apply {
            isCanFingerScan = true
            isEnableConfirmBtn = false
            lifecycleOwner = this.lifecycleOwner
        }

    }

    override fun listener() {
        binding.apply {
            btnConfirm.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    //gotoSummary()

                    if(isValidate()){
                        val pin = spfOtp.text.toString()
                        authViewModel.authPin(pin)
                    }
                }
            }

            imgFingerScan.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    showBiometricPrompt(
                        { result ->
                            Log.d("LOGD", "listener: ${result}")
                            manageTransactionMode()
                        },
                        { errorCode, message ->
                            Toast.makeText(requireContext(), "${message}", Toast.LENGTH_SHORT).show()
                        })
                }
            }

            spfOtp.onTextCompleteListener = object : PinField.OnTextCompleteListener {
                override fun onTextComplete(enteredText: String): Boolean {
                    binding.isEnableConfirmBtn = true
                    return true
                }
            }
            spfOtp.doAfterTextChanged {
                if(it.toString().isNotEmpty()){
                    binding.isPinInvalid = false
                    binding.isPinEmpty = false
                }
            }

        }
    }

    override fun subscribe() {
        super.subscribe()

        authViewModel.authPinLiveData.observe(viewLifecycleOwner){
            when (it) {
                is ResultWrapper.Loading -> {
                    progressDialog.show(childFragmentManager)
                }

                is ResultWrapper.Success -> {
                    progressDialog.dismiss()
                    if(it.response.success == true){
                        manageTransactionMode()
                    }
                }

                is ResultWrapper.GenericError -> {
                    progressDialog.dismiss()
                    binding.isPinInvalid = true
                }

                is ResultWrapper.NetworkError -> {
                    binding.isPinInvalid = true
                }

                else -> { binding.isPinInvalid = true }
            }
        }

        topupViewModel.topUpLiveData.observe(this) {
            when (it) {
                is ResultWrapper.Loading -> {
                    showLoading()
                }

                is ResultWrapper.GenericError -> {
                    AppAlert.alertGenericError(requireContext(), it.code, it.message).show(childFragmentManager)
                    hideLoading()
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alertNetworkError(requireContext()).show(childFragmentManager)
                    hideLoading()
                }

                is ResultWrapper.Success -> {
                    hideLoading()
                    topUpSuccess()
                }

                else -> {
                    /*none*/
                }
            }
        }

        exchangeViewModel.exchangeLiveData.observe(this) {
            when (it) {
                is ResultWrapper.Loading -> {
                    showLoading()
                }

                is ResultWrapper.GenericError -> {
                    AppAlert.alertGenericError(requireContext(), it.code, it.message).show(childFragmentManager)
                    hideLoading()
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alertNetworkError(requireContext()).show(childFragmentManager)
                    hideLoading()
                }

                is ResultWrapper.Success -> {
                    exchangeSuccess()
                    hideLoading()
                }

                else -> hideLoading()
            }
        }

        withdrawViewModel.withdrawLiveData.observe(this) {
            when (it) {
                is ResultWrapper.Loading -> {
                    showLoading()
                }

                is ResultWrapper.GenericError -> {
                    AppAlert.alertGenericError(requireContext(), it.code, it.message).show(childFragmentManager)
                    hideLoading()
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alertNetworkError(requireContext()).show(childFragmentManager)
                    hideLoading()
                }

                is ResultWrapper.Success -> {
                    gotoSummary(it.response)
                    hideLoading()
                }

                else -> hideLoading()
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    private fun isValidate(): Boolean {
        var result = false
        with(binding) {
            val pin = spfOtp.text.toString()

            when {
                pin.isEmpty() || pin.length != AppConstant.PIN_LENGTH -> {
                    isPinEmpty = true
                }
                else -> result = true
            }
        }

        return result
    }

    private fun manageTransactionMode(){
        when(args.authBy){
            AuthByEnum.TOPUP -> {
                topupViewModel.topUp(amount = args.amount.toDouble())
            }
            AuthByEnum.EXCHANGE -> {
                exchangeViewModel.exchange()
            }
            AuthByEnum.WITHDRAW -> {
                withdrawViewModel.withdraw(amount = args.amount.toDouble())
            }
            else ->{
                AppNavigator(requireActivity()).goToMain()
            }
        }
    }

    private fun topUpSuccess() {
        val title = resources.getString(R.string.message_topup_success)
        val btn = resources.getString(R.string.button_back_to_main)
        showAlertSuccessDialog(title, btn) {
            activity?.finish()
        }
    }

    private fun exchangeSuccess() {
        val msg = resources.getString(R.string.message_exchange_success)
        val btn = resources.getString(R.string.button_back_to_main)
        showAlertSuccessDialog(msg, btn) {
            activity?.finish()
        }
    }

    private fun gotoSummary(resp : WithdrawResponseModel) {
        val action = AuthWithBioFragmentDirections.actionAuthWithBioFragmentToWithDrawSummaryFragment(resp)
        WithDrawSummaryFragment.navigate(this@AuthWithBioFragment,action)
    }
}
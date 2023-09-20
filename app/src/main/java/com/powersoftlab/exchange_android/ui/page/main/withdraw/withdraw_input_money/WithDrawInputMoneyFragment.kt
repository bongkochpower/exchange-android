package com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_input_money

import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.enum.AuthByEnum
import com.powersoftlab.exchange_android.databinding.FragmentWithdrawInputMoneyBinding
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setTextSlideButtonEnable
import com.powersoftlab.exchange_android.ext.toStringFormat
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.withdraw.WithdrawViewModel
import com.powersoftlab.exchange_android.ui.page.main.withdraw.auth_with_bio.AuthWithBioFragment
import com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_type.WithDrawTypeFragmentDirections
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class WithDrawInputMoneyFragment : BaseFragment<FragmentWithdrawInputMoneyBinding>(R.layout.fragment_withdraw_input_money),OnBackPressedFragment {

    private val withdrawViewModel : WithdrawViewModel by sharedStateViewModel()

    companion object {
        fun newInstance() = WithDrawInputMoneyFragment()
        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(WithDrawTypeFragmentDirections.actionWithDrawTypeFragmentToWithDrawInputMoneyFragment())
    }

    override fun setUp() {
        setupWithdrawData()
    }

    override fun listener() {
        binding.apply {

            edtMoneyAmount.doAfterTextChanged {
                if (isWithdrawInvalid == true) {
                    isWithdrawInvalid = false
                }
                slideToConfirm.setTextSlideButtonEnable(it.toString().isNotEmpty(),R.string.button_slide_to_topup)
            }


            slideToConfirm.setTextSlideButtonEnable(false,R.string.button_slide_to_withdraw)
            slideToConfirm.setOnClickListener {
                if (!isMonoClickable()) return@setOnClickListener
                monoLastTimeClick()

                slideToConfirm.setEnable(false)
                slideToConfirm.setBackgroundRes(R.drawable.bg_slide_confirm_done)


                if(isValidate()){
                    /*
                    viewModel.topUp(amount = amount)*/
                    gotoAuthBio()
                }else{
                    slideToConfirm.setTextSlideButtonEnable(true,R.string.button_slide_to_withdraw)
                }

            }
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    private fun isValidate(): Boolean {
        var isValidate = false
        with(binding) {
            val inputMoney = edtMoneyAmount.text.toString().trim().replace(",", "")
            val currentBalance = withdrawViewModel.selectedCurrency?.balance ?: 0.0
            when {
                inputMoney.isEmpty() || inputMoney == "0" || inputMoney.toDouble() > currentBalance -> {
                    isWithdrawInvalid = true
                }

                else -> isValidate = true
            }
        }
        return isValidate
    }

    private fun gotoAuthBio() {
        val amount = binding.edtMoneyAmount.text.toString().replace(",", "").toDoubleOrNull() ?: 0.0
        val action = WithDrawInputMoneyFragmentDirections.actionWithDrawInputMoneyFragmentToAuthWithBioFragment(AuthByEnum.WITHDRAW,amount.toFloat())
        AuthWithBioFragment.navigate(this@WithDrawInputMoneyFragment,action)
    }

    private fun setupWithdrawData() {
        val currentData = withdrawViewModel.selectedCurrency
        binding.apply {
            tvCurrencyType.text = "(${currentData?.label})"
            tvShowYourBalance.text = "${getString(R.string.hint_withdraw_your_balance)} ${currentData?.balance?.toStringFormat()} ${currentData?.label}"
        }
    }



}
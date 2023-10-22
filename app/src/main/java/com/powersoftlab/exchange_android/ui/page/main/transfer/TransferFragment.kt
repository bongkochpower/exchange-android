package com.powersoftlab.exchange_android.ui.page.main.transfer

import androidx.core.widget.doAfterTextChanged
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.databinding.FragmentTransferBinding
import com.powersoftlab.exchange_android.databinding.ItemRvExchangeCurrencyBinding
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setTextSlideButtonEnable
import com.powersoftlab.exchange_android.ext.toStringFormat
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.model.response.WalletDetailResponseModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter
import com.powersoftlab.exchange_android.ui.list.viewholder.bind.ExchangeHolderHelper.initExchangeFrom
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.transfer.transfer_detail.TransferDetailFragment
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class TransferFragment : BaseFragment<FragmentTransferBinding>(R.layout.fragment_transfer), OnBackPressedFragment {

    private val transferViewModel: TransferViewModel by sharedStateViewModel()

    private val currencyAdapter by lazy {
        SimpleRecyclerViewAdapter<UserModel.CustomerBalance, ItemRvExchangeCurrencyBinding>(
            layout = R.layout.item_rv_exchange_currency,
            isRunAnimation = true,
        )
    }

    companion object {
        fun newInstance() = TransferFragment()
    }

    override fun onResume() {
        super.onResume()
        getCurrency()
    }

    override fun setUp() {
        with(binding){
            rvCurrency.apply {
                adapter = currencyAdapter
            }
            currencyAdapter.initExchangeFrom(requireContext()) {
                updateCurrencySelected(it)
            }
        }
    }

    override fun listener() {
        with(binding){

            edtInputTransfer.doAfterTextChanged {
                if (isInputMoneyInvalid == true) {
                    isInputMoneyInvalid = false
                }

                val enable = it.toString().isNotEmpty() && edtAccountNumber.text.toString().isNotEmpty()
                slideToTransaction.setTextSlideButtonEnable(enable,R.string.button_slide_to_transaction)
            }

            edtAccountNumber.doAfterTextChanged {
                if (isAccountNumberInvalid == true) {
                    isAccountNumberInvalid = false
                }

                val enable = it.toString().isNotEmpty() && edtInputTransfer.text.toString().isNotEmpty()
                slideToTransaction.setTextSlideButtonEnable(enable,R.string.button_slide_to_transaction)
            }

            slideToTransaction.setTextSlideButtonEnable(false,R.string.button_slide_to_transaction)
            slideToTransaction.setOnClickListener {
                if (!isMonoClickable()) return@setOnClickListener
                monoLastTimeClick()

                slideToTransaction.setEnable(false)
                slideToTransaction.setBackgroundRes(R.drawable.bg_slide_confirm_done)

                if(isValidate()){
                    val walletId = edtAccountNumber.text.toString().trim()
                    transferViewModel.walletId = walletId
//                    transferViewModel.walletId = "65317cc32fb065317cd6e226"
                    transferViewModel.getProfileByWalletId()
                }else{
                    slideToTransaction.setTextSlideButtonEnable(true,R.string.button_slide_to_transaction)
                }

            }
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun subscribe() {
        super.subscribe()

        transferViewModel.profileByWalletIdRequestLiveData.observe(viewLifecycleOwner) {
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

                    it.response.data?.let {
                        gotoTransferDetail(it)
                    } ?: run {
                        AppAlert.alert(requireContext(),"WalletID Not Found").show(childFragmentManager)
                    }
                }

                else -> {
                    /*none*/
                }
            }
        }
    }

    private fun getCurrency() {
        val list = transferViewModel.getCurrency()
        list.first().isSelected = true
        currencyAdapter.submitList(true, list)

        updateCurrencySelected(list.first())

    }

    private fun updateCurrencySelected(currencyModel: UserModel.CustomerBalance) {
        val txtCurrency = "${resources.getString(R.string.hint_exchange_your_balance_from)} ${currencyModel.balance?.toStringFormat()} ${currencyModel.label}"
        binding.tvCurrencyForm.text = txtCurrency
        transferViewModel.selectedCurrency = currencyModel
    }

    private fun isValidate(): Boolean {
        var isValidate = false
        with(binding) {
            val inputMoney = edtInputTransfer.text.toString().trim().replace(",","")
            val accountNumber = edtAccountNumber.text.toString().trim()
            val currentBalance = transferViewModel.selectedCurrency?.balance ?: 0.0
            when {
                inputMoney.isEmpty() || inputMoney == "0" || inputMoney.toDouble() > currentBalance -> {
                    isInputMoneyInvalid = true
                }
                accountNumber.isEmpty() -> {
                    isAccountNumberInvalid = true
                }

                else -> isValidate = true
            }
        }
        return isValidate
    }

    private fun gotoTransferDetail(data : WalletDetailResponseModel) {
        binding.apply {
            val amount = edtInputTransfer.text.toString().replace(",", "").toDoubleOrNull() ?: 0.0
            transferViewModel.inputMoneyTransfer = amount


            val action = TransferFragmentDirections.actionTransferFragmentToTransferDetailFragment(data)
            TransferDetailFragment.navigate(this@TransferFragment,action)
        }
    }
}
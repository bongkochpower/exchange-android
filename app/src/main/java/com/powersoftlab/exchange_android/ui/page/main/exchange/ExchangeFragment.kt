package com.powersoftlab.exchange_android.ui.page.main.exchange

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.common.enum.AuthByEnum
import com.powersoftlab.exchange_android.databinding.FragmentExchangeBinding
import com.powersoftlab.exchange_android.databinding.ItemRvExchangeCurrencyBinding
import com.powersoftlab.exchange_android.ext.hideKeyboard
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ext.setTextSlideButtonEnable
import com.powersoftlab.exchange_android.ext.showKeyboard
import com.powersoftlab.exchange_android.ext.toStringFormat
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter
import com.powersoftlab.exchange_android.ui.list.viewholder.bind.ExchangeHolderHelper.initExchangeFrom
import com.powersoftlab.exchange_android.ui.list.viewholder.bind.ExchangeHolderHelper.initExchangeTo
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.withdraw.auth_with_bio.AuthWithBioFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import java.util.Timer
import kotlin.concurrent.schedule

class ExchangeFragment : BaseFragment<FragmentExchangeBinding>(R.layout.fragment_exchange), OnBackPressedFragment  {
    private val exchangeViewModel: ExchangeViewModel by sharedStateViewModel()
    private val exchangeFromAdapter by lazy {
        SimpleRecyclerViewAdapter<UserModel.CustomerBalance, ItemRvExchangeCurrencyBinding>(
            layout = R.layout.item_rv_exchange_currency,
            isRunAnimation = true,
        )
    }

    private val exchangeToAdapter by lazy {
        SimpleRecyclerViewAdapter<UserModel.CustomerBalance, ItemRvExchangeCurrencyBinding>(
            layout = R.layout.item_rv_exchange_currency,
            isRunAnimation = true,
        )
    }

    companion object{
        private const val DELAY = 1000L
    }

    override fun onResume() {
        super.onResume()
        getCurrencyExchangeFrom()
    }

    override fun setUp() {
        with(binding) {
            isStep2 = false

            rvExchangeFrom.apply {
                adapter = exchangeFromAdapter
            }
            rvExchangeTo.apply {
                adapter = exchangeToAdapter
            }

            exchangeFromAdapter.initExchangeFrom(requireContext()) {
                updateCurrencyFormSelected(it)
            }
            exchangeToAdapter.initExchangeTo(requireContext()) {
                updateCurrencyFormSelected(it, false)
            }
        }
    }

    override fun listener() {
        with(binding) {

            edtCurrencyFrom.addTextChangedListener(object : TextWatcher {
                var timer = Timer()
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    timer.cancel()
                }

                override fun afterTextChanged(it: Editable?) {
                    slideToExchange.setTextSlideButtonEnable(it.toString().isNotEmpty(),R.string.button_slide_to_exchange)
                    if (it.isNullOrEmpty()) {
                        tvResultCurrency.text = "0.00"
                    } else {
                        timer.cancel()
                        timer = Timer()
                        timer.schedule(DELAY) {
                            if (it.isNotEmpty()) {
                                val amount = it.toString().trim().replace(",", "").toDoubleOrNull() ?: 0.0
                                exchangeViewModel.amount = amount
                                lifecycleScope.launch {
                                    exchangeViewModel.exchangeCalculate()
                                }
                            }
                        }
                    }

                    if (isExchangeInvalid == true) {
                        isExchangeInvalid = false
                    }
                }
            })

            btnContinue.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    gotoStep2()
                }
            }

            slideToExchange.setTextSlideButtonEnable(false,R.string.button_slide_to_exchange)
            slideToExchange.setOnClickListener {
                if (!isMonoClickable()) return@setOnClickListener
                monoLastTimeClick()

                slideToExchange.setEnable(false)
                slideToExchange.setBackgroundRes(R.drawable.bg_slide_confirm_done)

                if (isValidate()) {
                    //exchange()
                    gotoAuthBio()
                }else{
                    slideToExchange.setTextSlideButtonEnable(true,R.string.button_slide_to_exchange)
                }

            }
        }
    }


    override fun subscribe() {
        super.subscribe()

        exchangeViewModel.exchangeCalculateLiveData.observe(this) {
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
                    if(getCurrentStep() == 1){
                        val txtCurrencyValueChange = resources.getString(R.string.hint_exchange_to_other_currency, exchangeViewModel.currencyFrom?.label,it.response.value?.toStringFormat() , exchangeViewModel.currencyTo?.label)
                        binding.tvValueChangePre.text = txtCurrencyValueChange
                    }else{
                        binding.tvResultCurrency.text = it.response.value?.toStringFormat()
                    }
                }

                else -> {
                    hideLoading()
                }
            }
        }



    }

    private fun getCurrencyExchangeFrom() {
        val list = exchangeViewModel.getCurrencyForm()
        list.first().isSelected = true
        exchangeFromAdapter.submitList(true, list)
        //date on start
        updateCurrencyFormSelected(list.first())

    }

    private fun getCurrencyExchangeTo(selectedForm: UserModel.CustomerBalance,isInit : Boolean = true) {
        val list = exchangeViewModel.getCurrencyForm()
        val listFiltered = list.filter { it.label != selectedForm.label }
        listFiltered.find { it.id == exchangeViewModel.currencyTo?.id }?.isSelected = true

        var itemIndex = exchangeToAdapter.getOriginalList().map { it.apply { it.isSelected = false } }.indexOf(selectedForm)
        if(itemIndex < 0 ) {
            itemIndex = 0
        }
        exchangeToAdapter.submitList(true, listFiltered.toMutableList())

        updateCurrencyFormSelected(listFiltered.find { it.isSelected } ?: listFiltered[itemIndex], false)
    }

    private fun updateCurrencyFormSelected(currencyModel: UserModel.CustomerBalance, isCurrencyFrom: Boolean = true) {
        val txtCurrency = "${resources.getString(R.string.hint_exchange_your_balance_from)} ${currencyModel.balance?.toStringFormat()} ${currencyModel.label}"

        if (isCurrencyFrom) {
            if (!exchangeFromAdapter.isLoading) {
                binding.tvCurrencyForm.text = txtCurrency
                exchangeViewModel.currencyFrom = currencyModel
                lifecycleScope.launch {
                    getCurrencyExchangeTo(currencyModel,false)
                }

            }
        } else {
            if (!exchangeToAdapter.isLoading) {
                binding.tvCurrencyTo.text = txtCurrency
                exchangeViewModel.currencyTo = currencyModel

            }

            //update result
            exchangeViewModel.amount = 1.0
            lifecycleScope.launch {
                exchangeViewModel.exchangeCalculate()
            }
        }


    }

    private fun getCurrentStep(): Int = if (binding.btnContinue.isVisible) {
        1
    } else {
        2
    }

    private fun gotoStep1() {
        with(binding) {
            hideKeyboard()
            //toolbar.setIcon(R.drawable.icon_close)
            edtCurrencyFrom.visibility = View.GONE
            isExchangeInvalid = false
            isStep2 = false
        }
    }

    private fun gotoStep2() {
        with(binding) {
            showKeyboard()
            isStep2 = true
            //toolbar.setIcon(R.drawable.icon_back)
            edtCurrencyFrom.apply {
                visibility = View.VISIBLE
                requestFocus()
                text?.clear()
            }
            tvResultCurrency.text = "0.00"
        }
    }

    private fun isValidate(): Boolean {
        var isValidate = false
        with(binding) {
            val inputMoney = edtCurrencyFrom.text.toString().trim().replace(",", "")
            val amountFrom = exchangeViewModel.currencyFrom?.balance ?: 0.0
            when {
                inputMoney.isEmpty() || inputMoney == "0" || inputMoney.toDouble() > amountFrom -> {
                    isExchangeInvalid = true
                }

                else -> isValidate = true
            }
        }
        return isValidate
    }

    private fun gotoAuthBio() {
        val action = ExchangeFragmentDirections.actionExchangeFragmentToAuthWithBioFragmentExchange(AuthByEnum.EXCHANGE,0.0F)
        AuthWithBioFragment.navigate(this@ExchangeFragment,action)
    }

    override fun onBackPressed(): Boolean{
        if (getCurrentStep() == 1) {
            return false
        } else {
            gotoStep1()
            return true
        }
    }
}
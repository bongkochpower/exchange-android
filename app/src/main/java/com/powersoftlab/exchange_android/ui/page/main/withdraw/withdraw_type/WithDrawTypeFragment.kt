package com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_type

import android.view.View
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.FragmentWithdrawTypeBinding
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.ui.dialog.bottomsheet.OptionMenuBottomSheetDialog
import com.powersoftlab.exchange_android.ui.dialog.bottomsheet.OptionMenuModel
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.withdraw.WithdrawViewModel
import com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_input_money.WithDrawInputMoneyFragment
import io.github.g00fy2.quickie.ScanQRCode
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class WithDrawTypeFragment : BaseFragment<FragmentWithdrawTypeBinding>(R.layout.fragment_withdraw_type), OnBackPressedFragment {

    private val withdrawViewModel : WithdrawViewModel by sharedStateViewModel()

    private val currencyBottomSheetDialog: OptionMenuBottomSheetDialog<UserModel.CustomerBalance> by lazy {
        OptionMenuBottomSheetDialog.newInstance(
            getCurrency()
        )
    }

    private val countryBottomSheetDialog: OptionMenuBottomSheetDialog<String> by lazy {
        OptionMenuBottomSheetDialog.newInstance(
            mockCountry().map { OptionMenuModel(name = it, data = it) }.toMutableList()
        )
    }

    private val shopBottomSheetDialog: OptionMenuBottomSheetDialog<String> by lazy {
        OptionMenuBottomSheetDialog.newInstance(
            mockShop().map { OptionMenuModel(name = it, data = it) }.toMutableList()
        )
    }

    val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        // handle QRResult
    }

    companion object {
        fun newInstance() = WithDrawTypeFragment()
    }

    override fun setUp() {
        with(binding) {
            fragment = this@WithDrawTypeFragment
        }
    }

    override fun listener() {
        with(binding) {
            btnNextStep.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    if (!isMonoClickable()) return@setOnClickListener
                    monoLastTimeClick()

                    gotoInputMoney()

                }
            }

            btnScanQr.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    scanQrCodeLauncher.launch(null)
                }
            }

            currencyBottomSheetDialog.setOnItemSelectedListener { item ->
                edtCurrency.setText(item.name)
            }

            countryBottomSheetDialog.setOnItemSelectedListener { item ->
                edtCountry.setText(item.name)
            }
            shopBottomSheetDialog.setOnItemSelectedListener { item ->
                edtShop.setText(item.name)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    private fun gotoInputMoney() {
        WithDrawInputMoneyFragment.navigate(this@WithDrawTypeFragment)
    }

    fun selectCurrency(view: View) {
        currencyBottomSheetDialog.show(childFragmentManager)
    }

    fun selectCountry(view: View) {
        countryBottomSheetDialog.show(childFragmentManager)
    }

    fun selectShop(view: View) {
        shopBottomSheetDialog.show(childFragmentManager)
    }


    /*mock data*/
    private fun getCurrency(): MutableList<OptionMenuModel<UserModel.CustomerBalance>> {
        val list = withdrawViewModel.getCurrency()
        return list.map { OptionMenuModel(name = it.currencyName , data = it) }.toMutableList()
    }

    private fun mockCountry(): MutableList<String> {
        return mutableListOf("THAILAND", "USA", "JAPAN")
    }

    private fun mockShop(): MutableList<String> {
        return mutableListOf<String>(
            "SHOP1", "SHOP2", "SHOP3", "SHOP4"
        )
    }


}
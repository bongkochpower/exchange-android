package com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_type

import android.view.View
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.databinding.FragmentWithdrawTypeBinding
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.dialog.bottomsheet.OptionMenuBottomSheetDialog
import com.powersoftlab.exchange_android.ui.dialog.bottomsheet.OptionMenuModel
import com.powersoftlab.exchange_android.ui.dialog.bottomsheet.convertToModel
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

    private lateinit var countryBottomSheetDialog: OptionMenuBottomSheetDialog<UserModel.CountryModel>

    private lateinit var shopBottomSheetDialog: OptionMenuBottomSheetDialog<UserModel.ShopModel>

    val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        // handle QRResult
    }

    private var mCountryModel : MutableList<UserModel.CountryModel>? = null

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
                withdrawViewModel.selectedCurrency = item.convertToModel(UserModel.CustomerBalance::class.java)
                withdrawViewModel.getCountryList()
            }

        }
    }

    override fun subscribe() {
        super.subscribe()
        withdrawViewModel.countryRequestLiveData.observe(this) {
            when (it) {
                is ResultWrapper.Loading -> {
                }

                is ResultWrapper.GenericError -> {
                    AppAlert.alertGenericError(requireContext(), it.code, it.message).show(childFragmentManager)
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alertNetworkError(requireContext()).show(childFragmentManager)
                }

                else -> {
                    /*none*/
                }
            }
        }
        withdrawViewModel.countryResultLiveData.observe(this) {
            it?.let { listItem ->
                setCountry(listItem)
            }
        }

        withdrawViewModel.shopByIdLiveData.observe(viewLifecycleOwner){
            when(it){
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    it.response.data?.let { itemList ->
                        setShop(itemList.toMutableList())
                    }
                }
                is ResultWrapper.GenericError -> AppAlert.alertGenericError(requireContext(), it.code, it.message).show(childFragmentManager)
                is ResultWrapper.NetworkError -> AppAlert.alertNetworkError(requireContext()).show(childFragmentManager)
                else -> {}
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

    private fun setCountry(list : MutableList<UserModel.CountryModel>) {
        val countryList = list.map { OptionMenuModel(name = it.nameTh , data = it) }.toMutableList()
        countryBottomSheetDialog = OptionMenuBottomSheetDialog.newInstance(countryList)
        countryBottomSheetDialog.setOnItemSelectedListener { item ->

            withdrawViewModel.selectedCountry = item.convertToModel(UserModel.CountryModel::class.java)
            binding.edtCountry.setText(item.name)
            withdrawViewModel.getShopById()

        }
    }

    private fun setShop(list : MutableList<UserModel.ShopModel>) {
        val shopList = list.map { OptionMenuModel(name = it.shopName , data = it) }.toMutableList()
        shopBottomSheetDialog = OptionMenuBottomSheetDialog.newInstance(shopList)
        shopBottomSheetDialog.setOnItemSelectedListener { item ->
            binding.edtShop.setText(item.name)
            withdrawViewModel.selectedShop = item.convertToModel(UserModel.ShopModel::class.java)
        }
    }


}
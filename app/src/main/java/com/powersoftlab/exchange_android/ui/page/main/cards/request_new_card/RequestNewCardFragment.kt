package com.powersoftlab.exchange_android.ui.page.main.cards.request_new_card

import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.databinding.FragmentRequestNewCardBinding
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ext.toDashWhenNullOrEmpty
import com.powersoftlab.exchange_android.model.body.RequestNewCardRequestModel
import com.powersoftlab.exchange_android.model.response.AddressAutoFillResponseModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.dialog.bottomsheet.AddressAutoFillBottomSheetDialog
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.login.register.register.RegisterViewModel
import com.powersoftlab.exchange_android.ui.page.main.cards.CardsFragmentDirections
import com.powersoftlab.exchange_android.ui.page.main.cards.CardsViewModel
import com.powersoftlab.exchange_android.ui.widget.EdittextRegister
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class RequestNewCardFragment : BaseFragment<FragmentRequestNewCardBinding>(R.layout.fragment_request_new_card), OnBackPressedFragment {

    private val cardViewMode : CardsViewModel by sharedStateViewModel()
    private val registerViewModel: RegisterViewModel by stateViewModel()
    private val appManager : AppManager by inject()
    private var mAddressAutoFillData : AddressAutoFillResponseModel? = null

    private val subDistrictBottomSheetDialog: AddressAutoFillBottomSheetDialog by lazy {
        val subDistricts = appManager.getSubDistricts() ?: mutableListOf()
        AddressAutoFillBottomSheetDialog.newInstance(subDistricts.toMutableList())
    }

    companion object {
        fun newInstance() = RequestNewCardFragment()

        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(CardsFragmentDirections.actionCardsFragmentToRequestNewCardFragment())
    }

    override fun setUp() {
        gotoStepInfo()

        binding.apply {

        }

    }

    override fun listener() {
        with(binding) {

            layoutEnterAddress.apply {
                edtRegHouseNo.doAfterTextChanged {
                    if(isHouseNoEmpty == true){
                        isHouseNoEmpty = false
                    }
                }
                edtRegPostcode.doAfterTextChanged {
                    if(isPostcodeEmpty == true){
                        isPostcodeEmpty = false
                    }
                }

                edtRegSubDistrict.setOnClickListener {
                    subDistrictBottomSheetDialog.show(childFragmentManager)
                    subDistrictBottomSheetDialog.setOnItemSelectedListener {
                        registerViewModel.getAddressDataById(it)
                    }

                }
//                edtRegDistrict.validateAfterTextChange()
//                edtRegProvince.validateAfterTextChange()
            }

            btnConfirm.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    if (!isMonoClickable()) return@setOnClickListener
                    monoLastTimeClick()

                    if (getCurrentStep() == 1) {
                        if (isValidate(getRegisterNewCardData())) {
                            cardViewMode.requestNewCard(getRegisterNewCardData())
                        }
                        //gotoStepDetail()
                    } else {
                        Log.d("LOGD", "listener: call api new card")
                        activity?.finish()
                    }
                }
            }
        }

    }

    override fun subscribe() {
        super.subscribe()

        cardViewMode.requestNewCardResult().observe(viewLifecycleOwner){
            when (it) {
                is ResultWrapper.Loading -> {
                    showProgress()
                }
                is ResultWrapper.Success -> {
                    hideProgress()
                    gotoStepDetail()
                }
                is ResultWrapper.GenericError -> {
                    hideProgress()
                    AppAlert.alertGenericError(requireContext(), it.code, it.message).show(childFragmentManager)
                }

                is ResultWrapper.NetworkError -> {
                    hideProgress()
                    AppAlert.alertNetworkError(requireContext()).show(childFragmentManager)
                }

                else -> {
                    /*none*/
                }
            }
        }

        registerViewModel.addressByIdLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {}

                is ResultWrapper.Success -> {
                    val data = it.response.data
                    data?.let { it1 -> updateAddressData(it1) }

                }

                is ResultWrapper.GenericError -> {
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                else -> {}
            }
        }
    }

    private fun getCurrentStep(): Int = if (binding.layoutEnterAddress.root.isVisible) {
        1
    } else {
        2
    }

    private fun gotoStepInfo() {
        binding.apply {
            isStepInfo = true
            cardViewMode.setIcon(R.drawable.icon_back)

            //prepare data for register
            setupDataRequestNewCard()
        }
    }

    private fun gotoStepDetail() {
        binding.apply {
            isStepInfo = false
            cardViewMode.setIcon(0)

        }
    }

    override fun onBackPressed(): Boolean {
        return getCurrentStep() != 1
    }

    private fun isValidate(item: RequestNewCardRequestModel): Boolean {
        var isValidate = false
        with(binding.layoutEnterAddress) {
            when {
                item.houseNo?.isEmpty() == true -> {
                    isHouseNoEmpty = true
                }

                item.subDistrict.isNullOrEmpty() -> {
                    isSubDistrictEmpty = true
                    validateEdittextErrorView(edtRegSubDistrict)
                }

                item.district.isNullOrEmpty() -> {
                    isDistrictEmpty = true
                    validateEdittextErrorView(edtRegDistrict)
                }

                item.province.isNullOrEmpty() -> {
                    isProvinceEmpty = true
                    validateEdittextErrorView(edtRegProvince)
                }

                item.country.isNullOrEmpty() -> {
                    validateEdittextErrorView(edtRegCountry)
                }

                item.postCode.isNullOrEmpty() -> {
                    isPostcodeEmpty = true
                }

                else -> {
                    isValidate = true
                }

            }
        }

        return isValidate
    }

    private fun validateEdittextErrorView(v: View, tvError: TextView? = null) {
        val defError = "กรุณาใส่"
        v.requestFocus()

        val scrollTo: Int = (v.parent.parent as View).top + v.top
        //binding.nsv.smoothScrollTo(0, scrollTo)

        when (v) {
            is EdittextRegister -> {
                v.setValidation(true, "$defError${v.getHintText()}")
            }

            is EditText -> {
                v.requestFocus()
                tvError?.text = "$defError${v.hint}"
            }
        }
    }

    private fun getRegisterNewCardData(): RequestNewCardRequestModel {
        with(binding.layoutEnterAddress) {
            return RequestNewCardRequestModel(
                postCode = edtRegPostcode.text.trim().toString(),
//                moo = edtRegVillageNo.text.trim().toString(),
//                soi = edtRegAlley.text.trim().toString(),
                houseNo = edtRegHouseNo.text.trim().toString(),
                village = edtRegVillage.text.trim().toString(),
                road = edtRegStreet.getText(),
                districtId = mAddressAutoFillData?.district?.districtId,
                district = edtRegDistrict.getText(),
                subDistrictId = mAddressAutoFillData?.subDistrict?.id,
                subDistrict = edtRegSubDistrict.text.trim().toString(),
                province = edtRegProvince.getText(),
                provinceId = mAddressAutoFillData?.province?.provinceId,
                country = edtRegCountry.getText(),
            )
        }
    }

    private fun setupDataRequestNewCard(){
        val userItem = appManager.getUser()
        with(binding.layoutEnterAddress){
            userItem?.let {
                edtRegHouseNo.setText(it.houseNo)
                edtRegVillage.setText(it.village)
//                edtRegVillageNo.setText(it.moo)
//                edtRegAlley.setText(it.soi)
                edtRegStreet.setText(it.road.toDashWhenNullOrEmpty())
                edtRegPostcode.setText(it.postCode)
                it.subDistictId?.let { registerViewModel.getAddressDataById(it) }
                edtRegCountry.setText("Thailand")
            }

        }
    }

    private fun updateAddressData(data : AddressAutoFillResponseModel){
        mAddressAutoFillData = data
        with(binding.layoutEnterAddress){
            edtRegSubDistrict.setText(data.subDistrict?.nameEN.toDashWhenNullOrEmpty())
            edtRegDistrict.setText(data.district?.nameEN.toDashWhenNullOrEmpty())
            edtRegProvince.setText(data.province?.nameEN.toDashWhenNullOrEmpty())
            edtRegPostcode.setText(data.subDistrict?.zipCode.toDashWhenNullOrEmpty())
        }
    }

    private fun showProgress() = progressDialog.show(childFragmentManager)
    private fun hideProgress() = progressDialog.dismiss()

}
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
import com.powersoftlab.exchange_android.databinding.FragmentRequestNewCardBinding
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ext.validateAfterTextChange
import com.powersoftlab.exchange_android.model.body.RequestNewCardRequestModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.cards.CardsFragmentDirections
import com.powersoftlab.exchange_android.ui.page.main.cards.CardsViewModel
import com.powersoftlab.exchange_android.ui.widget.EdittextRegister
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class RequestNewCardFragment : BaseFragment<FragmentRequestNewCardBinding>(R.layout.fragment_request_new_card), OnBackPressedFragment {

    private val cardViewMode : CardsViewModel by sharedStateViewModel()

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

                edtRegSubDistrict.validateAfterTextChange()
                edtRegDistrict.validateAfterTextChange()
                edtRegProvince.validateAfterTextChange()
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
                moo = edtRegVillageNo.text.trim().toString(),
                soi = edtRegAlley.text.trim().toString(),
                houseNo = edtRegHouseNo.text.trim().toString(),
                village = edtRegVillage.text.trim().toString(),
                road = edtRegStreet.getText(),
                districtId = 1,
                district = edtRegDistrict.getText(),
                subDistrictId = 1,
                subDistrict = edtRegSubDistrict.getText(),
                province = edtRegProvince.getText(),
                provinceId = 1,
                country = edtRegCountry.getText(),
            )
        }
    }

    private fun showProgress() = progressDialog.show(childFragmentManager)
    private fun hideProgress() = progressDialog.dismiss()

}
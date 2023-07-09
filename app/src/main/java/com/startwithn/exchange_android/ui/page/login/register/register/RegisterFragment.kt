package com.startwithn.exchange_android.ui.page.login.register.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.common.constant.AppConstant.PHONE_NUMBER_LENGTH
import com.startwithn.exchange_android.databinding.FragmentRegisterBinding
import com.startwithn.exchange_android.ext.fadeIn
import com.startwithn.exchange_android.ext.getPhotoFromGallery
import com.startwithn.exchange_android.ext.gone
import com.startwithn.exchange_android.ext.isEmail
import com.startwithn.exchange_android.ext.isMonoClickable
import com.startwithn.exchange_android.ext.loadImageCircle
import com.startwithn.exchange_android.ext.monoLastTimeClick
import com.startwithn.exchange_android.ext.setOnTouchAnimation
import com.startwithn.exchange_android.ext.takePhoto
import com.startwithn.exchange_android.model.body.RegisterRequestModel
import com.startwithn.exchange_android.network.builder.RetrofitBuilder
import com.startwithn.exchange_android.ui.page.base.BaseFragment
import com.startwithn.exchange_android.ui.page.login.register.TermRegisterFragmentDirections
import com.startwithn.exchange_android.ui.widget.EdittextRegister
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import java.io.File

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    private val registerViewModel: RegisterViewModel by stateViewModel()

    companion object {
        fun newInstance() = RegisterFragment()

        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(TermRegisterFragmentDirections.actionTermRegisterFragmentToRegisterFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            fadeIn()
        }
    }

    override fun setUp() {
        binding.apply {
            fragment = this@RegisterFragment
        }

    }

    override fun listener() {
        with(binding) {
            imgSelectProfile.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    if (!isMonoClickable()) return@setOnClickListener
                    monoLastTimeClick()

                    activity?.getPhotoFromGallery { resultCode, data ->
                        uploadProfileImage(resultCode, data)

                    }

                }
            }

            edtRegName.addOnTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    val isNotEmpty = p0?.toString().orEmpty().isNotEmpty()
                    binding.edtRegName.setValidation(!isNotEmpty)
                }
            })

            edtRegConfirmPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString().isNotEmpty() && binding.isConfirmPwNotMatch == true) {
                        isConfirmPwNotMatch = false
                    }
                }
            })
        }
    }

    private fun fadeIn() {
        with(binding) {
            llRegisterForm.fadeIn()
        }
    }

    private fun uploadProfileImage(resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                data?.let { uri ->
                    binding.imgProfile.loadImageCircle(uri.data)
                    binding.llProfileDummy.gone()
                    //upload
                    uri.data?.path?.let {
                        val file: File = File(it)
                        val reqFile = file.asRequestBody(RetrofitBuilder.MEDIA_TYPE_FILE)
                        reqFile.let { it1 ->
                            /*profileViewModel.updateAvatar(
                                MultipartBody.Part.createFormData("avatar", file.name, it1)
                            )*/
                        }
                    }
                }
            }

            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onRegister(v: View) {
        v.setOnTouchAnimation()
        if (!isMonoClickable()) return
        monoLastTimeClick()

        if (isValidateRegister()) {
            register()
        }
    }

    private fun isValidateRegister(): Boolean {
        var isValidate = true
        with(binding) {
            val defError = "กรุณาใส่"
            val name = edtRegName.getText()
            val lastname = edtRegLastname.getText()
            val phone = edtRegPhone.text.toString().trim()
            val email = edtRegEmail.getText()
            val dob = edtRegDob.text.toString()
            val houseNo = edtRegHouseNo.text.toString()
            val subDistrict = edtRegSubDistrict.getText()
            val district = edtRegDistrict.getText()
            val province = edtRegProvince.getText()
            val pw = edtRegPassword.text.trim().toString()
            val confirmPw = edtRegConfirmPassword.text.trim().toString()

            when {
                name.isEmpty() -> validateEdittextErrorView(edtRegName)
                lastname.isEmpty() -> validateEdittextErrorView(edtRegLastname)
                phone.isEmpty() -> {
                    isPhoneEmpty = true
                    tvErrorPhone.text = "$defError${edtRegPhone.hint}"
                    binding.nsv.smoothScrollTo(0, 0)
                }

                phone.length < PHONE_NUMBER_LENGTH -> {
                    isPhoneEmpty = true
                    tvErrorPhone.text = getString(R.string.validate_reg_phone_10_digit)
                    binding.nsv.smoothScrollTo(0, 0)
                }
                !email.isEmail() ->  validateEdittextErrorView(edtRegEmail)


                pw != confirmPw -> {
                    isConfirmPwNotMatch = true
                }
            }
        }
        return isValidate
    }

    private fun validateEdittextErrorView(v: View) {
        v.requestFocus()
        binding.nsv.smoothScrollTo(0, 0)

        when (v) {
            is EdittextRegister -> {
                v.setValidation(true)
            }

            is EditText -> {
                v.requestFocus()
            }
        }
    }

    private fun register() {
        with(binding) {
            registerViewModel.register(
                RegisterRequestModel(
                    id = null,
                    isActivate = null,
                    firstname = null,
                    lastname = null,
                    birtDate = null,
                    tel = null,
                    email = null,
                    password = null,
                    idCardImagePath = null,
                    address = null,
                    postCode = null,
                    moo = null,
                    soi = null,
                    houseNo = null,
                    village = null,
                    road = null,
                    districtId = null,
                    district = null,
                    subDistrictId = null,
                    subDistrict = null,
                    province = null,
                    provinceId = null,
                    isConsent = null
                )
            )
        }
    }

}
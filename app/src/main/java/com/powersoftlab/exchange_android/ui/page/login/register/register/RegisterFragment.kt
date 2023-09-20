package com.powersoftlab.exchange_android.ui.page.login.register.register

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.*
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.common.constant.AppConstant.FORMAT_UI_DATE
import com.powersoftlab.exchange_android.common.constant.AppConstant.PHONE_NUMBER_LENGTH
import com.powersoftlab.exchange_android.common.enum.LoginTypeEnum
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.common.navigator.AppNavigator
import com.powersoftlab.exchange_android.databinding.FragmentRegisterBinding
import com.powersoftlab.exchange_android.ext.convertDisplayDateToBuddhistYear
import com.powersoftlab.exchange_android.ext.convertDisplayDateToChristianYear
import com.powersoftlab.exchange_android.ext.fadeIn
import com.powersoftlab.exchange_android.ext.gone
import com.powersoftlab.exchange_android.ext.isEmail
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.isValidPassword
import com.powersoftlab.exchange_android.ext.loadImageCircle
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ext.toDashWhenNullOrEmpty
import com.powersoftlab.exchange_android.ext.toDisplayFormat
import com.powersoftlab.exchange_android.ext.toServiceFormat
import com.powersoftlab.exchange_android.ext.toString
import com.powersoftlab.exchange_android.ext.uriToFile
import com.powersoftlab.exchange_android.ext.validateAfterTextChange
import com.powersoftlab.exchange_android.model.body.RegisterRequestModel
import com.powersoftlab.exchange_android.model.response.AddressAutoFillResponseModel
import com.powersoftlab.exchange_android.model.response.UserModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.network.builder.RetrofitBuilder
import com.powersoftlab.exchange_android.ui.dialog.bottomsheet.AddressAutoFillBottomSheetDialog
import com.powersoftlab.exchange_android.ui.dialog.bottomsheet.OptionMenuModel
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.intro.IntroViewModel
import com.powersoftlab.exchange_android.ui.page.login.LoginViewModel
import com.powersoftlab.exchange_android.ui.page.login.register.TermRegisterFragmentDirections
import com.powersoftlab.exchange_android.ui.page.login.register.set_auth.SetAuthenticationFragment
import com.powersoftlab.exchange_android.ui.widget.EdittextRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.Calendar


class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register), OnBackPressedFragment {

    private val registerViewModel: RegisterViewModel by stateViewModel()
    private val loginViewModel: LoginViewModel by sharedStateViewModel()

    //private var idCardPath: String? = null
    private var profileImagePath: String? = null
    private val appManager: AppManager by inject()
    private val user by lazy { appManager.getUser() }
    private lateinit var loginType: LoginTypeEnum
    private var mAddressAutoFillData : AddressAutoFillResponseModel? = null

    private val pickIdCardLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            uploadIdCardImage(uri)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private val pickIdCardForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                result?.data?.data?.let { imageUri ->
                    uploadIdCardImage(imageUri)
                }
            }
        }

    private val pickProfileLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            uploadProfileImage(uri)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private val subDistrictBottomSheetDialog: AddressAutoFillBottomSheetDialog by lazy {
        val subDistricts = appManager.getSubDistricts() ?: mutableListOf()
        AddressAutoFillBottomSheetDialog.newInstance(subDistricts.toMutableList())
    }

    companion object {
        fun newInstance() = RegisterFragment()

        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(TermRegisterFragmentDirections.actionTermRegisterFragmentToRegisterFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            //fadeIn()
        }
    }


    override fun setUp() {
        binding.apply {
            lifecycleOwner = this.lifecycleOwner
            fragment = this@RegisterFragment
        }

        loginType = appManager.getLoginType() ?: LoginTypeEnum.APP

        //test header
        //binding.tvRegisterTitle.text = getString(R.string.title_register_form).plus(loginType.name)

        manageLoginMode()

        //edit profile
        appManager.getUser()?.let {
            setupEditProfile(it)
        }
    }

    override fun listener() {
        with(binding) {
            imgSelectProfile.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    if (!isMonoClickable()) return@setOnClickListener
                    monoLastTimeClick()

//                    activity?.getPhotoFromGallery { resultCode, data ->
//                        uploadProfileImage(resultCode, data)
//                    }

                    pickProfileLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))


                }
            }

            edtRegSubDistrict.setOnClickListener {
                subDistrictBottomSheetDialog.show(childFragmentManager)
                subDistrictBottomSheetDialog.setOnItemSelectedListener {
                    registerViewModel.getAddressDataById(it)
                }

            }

            edtRegName.validateAfterTextChange()
            edtRegLastname.validateAfterTextChange()
            edtRegEmail.validateAfterTextChange()
            edtRegDistrict.validateAfterTextChange()
            edtRegProvince.validateAfterTextChange()

            edtRegPostcode.doAfterTextChanged {
                if (it.toString().isNotEmpty() && binding.isPostcodeEmpty == true) {
                    isPostcodeEmpty = false
                }
            }

            edtRegPhone.doAfterTextChanged {
                if (it.toString().isNotEmpty() && binding.isPhoneEmpty == true) {
                    isPhoneEmpty = false
                }
            }
            edtRegHouseNo.doAfterTextChanged {
                if (it.toString().isNotEmpty() && binding.isHouseNoEmpty == true) {
                    isHouseNoEmpty = false
                }
            }
            edtRegPassword.doAfterTextChanged {
                if (it.toString().isNotEmpty() && binding.isPasswordEmpty == true) {
                    isPasswordEmpty = false
                }

                if (!isValidPassword(it.toString()) && loginType.equals(LoginTypeEnum.APP)) {
                    isPasswordEmpty = true
                    tvErrorPassword.text = getString(R.string.validate_reg_password)
                }
            }
            edtRegConfirmPassword.doAfterTextChanged {
                if (it.toString().isNotEmpty() && binding.isConfirmPwNotMatch == true) {
                    isConfirmPwNotMatch = false
                }
            }
            chkRegTerm.setOnCheckedChangeListener { compoundButton, b ->
                chkRegTerm.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }

            btnRegister.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    if (!isMonoClickable()) return@setOnClickListener
                    monoLastTimeClick()

                    if (isValidateRegister()) {
                        user?.let {
                            updateProfile(it.id.toString())
                        } ?: run {
                            if (loginType != LoginTypeEnum.APP) {
                                updateProfile(appManager.getAuthToken().orEmpty())
                            } else {
                                register()
                            }
                        }

                    }

                    //test
//                    gotoSetPinPassword()

                }
            }
        }
    }

    override fun subscribe() {
        super.subscribe()

        registerViewModel.uploadImageProfileResult().observe(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.isAvatarUpdating = true
                }

                is ResultWrapper.Success -> {
                    binding.isAvatarUpdating = false
                    profileImagePath = it.response.fileName
                }

                is ResultWrapper.GenericError -> {
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                    binding.isAvatarUpdating = false
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                    binding.isAvatarUpdating = false
                }

                else -> binding.isAvatarUpdating = false
            }
        }
        registerViewModel.uploadIdCardImage.observe(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    binding.isIdCardUploading = true
                }

                is ResultWrapper.Success -> {
                    //idCardPath = it.response.fileName
                    registerViewModel.selectedIdCardImage = it.response.fileName
                    binding.isIdCardUploading = false
                }

                is ResultWrapper.GenericError -> {
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                    binding.isIdCardUploading = false
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                    binding.isIdCardUploading = false
                }

                else -> binding.isIdCardUploading = false
            }
        }
        registerViewModel.registerResult().observe(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    showLoading()
                }

                is ResultWrapper.Success -> {
                    hideLoading()

                    showAlertSuccessDialog {
                        //AppNavigator(this.requireActivity()).goToLogin(true)
                        gotoSetPinPassword()
                    }
                }

                is ResultWrapper.GenericError -> {
                    hideLoading()
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                is ResultWrapper.NetworkError -> {
                    hideLoading()
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                else -> hideLoading()
            }
        }

        registerViewModel.updateProfileLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {
                    showLoading()
                }

                is ResultWrapper.Success -> {
                    hideLoading()
                    val msg = resources.getString(R.string.message_edit_profile_success)
                    val btn = resources.getString(R.string.button_close)
                    showAlertSuccessDialog(title = msg, textButtonRight = btn) {
                        activity?.finish()
                    }
                }

                is ResultWrapper.GenericError -> {
                    hideLoading()
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                is ResultWrapper.NetworkError -> {
                    hideLoading()
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                else -> hideLoading()
            }
        }
        registerViewModel.addressByIdLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Loading -> {

                }

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

    private fun fadeIn() {
        with(binding) {
            llRegisterForm.fadeIn()
        }
    }


    private fun uploadProfileImage(uri: Uri) {
        binding.imgProfile.loadImageCircle(uri)
        binding.llProfileDummy.gone()
        //upload
        uri.path?.let {
            val file: File = uri.uriToFile(requireContext())
            val reqFile = file.asRequestBody(RetrofitBuilder.MEDIA_TYPE_FILE)
            reqFile.let { it1 ->
                registerViewModel.uploadImageProfile(
                    MultipartBody.Part.createFormData("image", file.name, it1)
                )
            }
        }
    }

    @SuppressLint("Range")
    private fun uploadIdCardImage(uri: Uri) {

        binding.isSelectIdCardImage = true
        binding.isIdCardImageEmpty = false
        //check file size
        val file = uri.uriToFile(requireContext())
        val fileSize = (file.length().div(1024)).toString().toInt()

        //update ui
        if ((fileSize.times(0.001)) > 25) { //25mb
            binding.apply {
                tvIdCardStatus.text = resources.getString(R.string.validate_reg_attach_file_fail)
                tvIdCardStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_attach_fail, 0, 0, 0);
            }
        } else {
            binding.apply {
                tvIdCardStatus.text = resources.getString(R.string.validate_reg_attach_file_success)
                tvIdCardStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_attach_success, 0, 0, 0);
            }
        }

        try {
            val filename: String = file.path.toString().substring(file?.path.toString().lastIndexOf("/") + 1)
            binding.tvIdCardName.text = getString(R.string.hint_attach_file, filename)
        } catch (e: Exception) {
            e.stackTrace
        }

        //upload
        val reqFile = file.asRequestBody(RetrofitBuilder.MEDIA_TYPE_FILE)
        reqFile.let { it1 ->
            registerViewModel.uploadIdCardImage(
                MultipartBody.Part.createFormData("image", file.name, it1)
            )
        }
    }

    fun selectBirthDate() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dob = cal.toString(FORMAT_UI_DATE)
            binding.edtRegDob.setText(dob.convertDisplayDateToBuddhistYear())
            binding.isDobEmpty = false

        }, year, month, day)
        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }

    fun selectIdCardImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickIdCardLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        pickIdCardForResult.launch(intent)
    }

    private fun isValidateRegister(): Boolean {
        var isValidate = false
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
            val postcode = edtRegPostcode.text.toString().trim()
            val pw = edtRegPassword.text.trim().toString()
            val confirmPw = edtRegConfirmPassword.text.trim().toString()
            val chkTerm = chkRegTerm.isChecked


            when {
                name.isEmpty() -> validateEdittextErrorView(edtRegName)
                lastname.isEmpty() -> validateEdittextErrorView(edtRegLastname)
                phone.isEmpty() -> {
                    isPhoneEmpty = true
                    validateEdittextErrorView(edtRegPhone, tvErrorPhone)
                }

                phone.length < PHONE_NUMBER_LENGTH -> {
                    isPhoneEmpty = true
                    tvErrorPhone.text = getString(R.string.validate_reg_phone_10_digit)
                    binding.nsv.smoothScrollTo(0, 0)
                }

                !email.isEmail() -> validateEdittextErrorView(edtRegEmail)
                dob.isEmpty() -> {
                    isDobEmpty = true
                    validateEdittextErrorView(edtRegDob)
                }

                isSelectIdCardImage == null -> {
                    isIdCardImageEmpty = true
                    binding.nsv.smoothScrollTo(0, 0)
                }

                houseNo.isEmpty() -> {
                    isHouseNoEmpty = true
                    validateEdittextErrorView(edtRegHouseNo)
                }

                subDistrict.isEmpty() -> validateEdittextErrorView(edtRegSubDistrict)
                district.isEmpty() -> validateEdittextErrorView(edtRegDistrict)
                province.isEmpty() -> validateEdittextErrorView(edtRegProvince)
                postcode.isEmpty() -> {
                    isPostcodeEmpty = true
                    validateEdittextErrorView(edtRegPostcode)
                }

                (pw.isEmpty() && loginType.equals(LoginTypeEnum.APP)) -> {
                    isPasswordEmpty = true
                    validateEdittextErrorView(edtRegPassword, tvErrorPassword)
                }

                (!isValidPassword(pw) && loginType.equals(LoginTypeEnum.APP)) -> {
                    isPasswordEmpty = true
                    //validateEdittextErrorView(edtRegPassword, tvErrorPassword)
                    tvErrorPassword.text = getString(R.string.validate_reg_password)
                }

                ((pw != confirmPw) && loginType.equals(LoginTypeEnum.APP)) -> {
                    isConfirmPwNotMatch = true
                    validateEdittextErrorView(edtRegConfirmPassword)
                }

                !chkTerm -> chkRegTerm.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_error_text))
                else -> isValidate = true
            }
        }
        return isValidate
    }

    private fun validateEdittextErrorView(v: View, tvError: TextView? = null) {
        val defError = "กรุณาใส่"
        v.requestFocus()

        val scrollTo: Int = (v.parent.parent as View).top + v.top
        binding.nsv.smoothScrollTo(0, scrollTo)

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



    private fun register() {
        getRegisterData().apply {
            isConsent = binding.chkRegTerm.isChecked
            profileImagePath = this@RegisterFragment.profileImagePath
        }.also {
            registerViewModel.register(it)
        }
    }

    private fun updateProfile(userId: String) {
        getRegisterData().apply {
            if (this@RegisterFragment.profileImagePath?.isNotEmpty() == true) {
                profileImagePath = this@RegisterFragment.profileImagePath
            }
        }.also {
            registerViewModel.updateProfile(userId, it)
        }
    }

    private fun getRegisterData(): RegisterRequestModel {
        with(binding) {
            val dobToServer = edtRegDob.text.trim().toString().convertDisplayDateToChristianYear().toServiceFormat()
            return RegisterRequestModel(
                firstname = edtRegName.getText(),
                lastname = edtRegLastname.getText(),
                birtDate = dobToServer,
                tel = edtRegPhone.text.trim().toString(),
                email = edtRegEmail.getText(),
                password = edtRegPassword.text.trim().toString(),
                idCardImagePath = registerViewModel.selectedIdCardImage ?: this@RegisterFragment.user?.idCardImage.toDashWhenNullOrEmpty(),
                postCode = edtRegPostcode.text.toString(),
                moo = edtRegVillageNo.text.trim().toString(),
                soi = edtRegAlley.text.trim().toString(),
                houseNo = edtRegHouseNo.text.trim().toString(),
                village = edtRegVillage.text.toString(),
                road = edtRegStreet.getText(),
                districtId = mAddressAutoFillData?.district?.districtId,
                district = edtRegDistrict.getText(),
                subDistrictId = mAddressAutoFillData?.subDistrict?.id,
                subDistrict = edtRegSubDistrict.text.trim().toString(),
                province = edtRegProvince.getText(),
                provinceId = mAddressAutoFillData?.province?.provinceId
            )
        }
    }

    private fun setupEditProfile(user: UserModel) {
        with(binding) {
            user.apply {

                tvRegisterTitle.text = resources.getString(R.string.title_register_edit_form)
                profileImage?.let {
                    imgProfile.loadImageCircle(it)
                    llProfileDummy.gone()
                }
                edtRegName.setText(firstName.orEmpty())
                edtRegLastname.setText(lastName.orEmpty())
                edtRegPhone.apply {
                    backgroundTintList = resources.getColorStateList(R.color.gray_alto)
                    setText(tel)
                    isEnabled = false
                }

                edtRegEmail.setText(email.orEmpty())
                edtRegDob.setText(birthDate.toDisplayFormat().convertDisplayDateToBuddhistYear())

                isSelectIdCardImage = true
                val filename: String = idCardImage.orEmpty()
                binding.tvIdCardName.text = getString(R.string.hint_attach_file, filename)

                edtRegHouseNo.setText(houseNo.orEmpty())
                edtRegVillage.setText(village.orEmpty())
                edtRegVillageNo.setText(moo.orEmpty())
                edtRegAlley.setText(soi.orEmpty())
                edtRegStreet.setText(road.orEmpty())
                //edtRegSubDistrict.setText(subDistict.orEmpty())
                //edtRegDistrict.setText(district.orEmpty())
                //edtRegProvince.setText(provine.orEmpty())
                //edtRegPostcode.setText(postCode.orEmpty())
                subDistictId?.let { registerViewModel.getAddressDataById(it) }
                btnRegister.setText(resources.getString(R.string.title_register_edit_form))
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    private fun manageLoginMode() {
        with(binding) {
            if (loginType != LoginTypeEnum.APP) {
                layoutPassword.gone()
            }
        }
    }


    private fun updateAddressData(data : AddressAutoFillResponseModel){
        mAddressAutoFillData = data
        with(binding){
            edtRegSubDistrict.setText(data.subDistrict?.nameTH.toDashWhenNullOrEmpty())
            edtRegDistrict.setText(data.district?.nameTH.toDashWhenNullOrEmpty())
            edtRegProvince.setText(data.province?.nameTH.toDashWhenNullOrEmpty())
            edtRegPostcode.setText(data.subDistrict?.zipCode.toDashWhenNullOrEmpty())
        }
    }

    private fun gotoSetPinPassword(){
        val action = RegisterFragmentDirections.actionRegisterFragmentToSetAuthenticationFragment()
        SetAuthenticationFragment.navigate(this@RegisterFragment,action)
    }

}
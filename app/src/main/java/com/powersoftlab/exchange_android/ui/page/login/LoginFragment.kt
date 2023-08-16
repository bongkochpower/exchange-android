package com.powersoftlab.exchange_android.ui.page.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.REQUEST_CODE
import com.linecorp.linesdk.LoginDelegate
import com.linecorp.linesdk.LoginListener
import com.linecorp.linesdk.Scope
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginApi
import com.linecorp.linesdk.auth.LineLoginResult
import com.linecorp.linesdk.widget.LoginButton
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.common.enum.SocialLoginTypeEnum
import com.powersoftlab.exchange_android.common.navigator.AppNavigator
import com.powersoftlab.exchange_android.databinding.FragmentLoginBinding
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ext.slideUp
import com.powersoftlab.exchange_android.model.body.LoginRequestModel
import com.powersoftlab.exchange_android.model.body.LoginSocialRequestModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.login.register.TermRegisterFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.util.Arrays
import kotlin.math.log


class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login), OnBackPressedFragment {

    private val loginViewModel: LoginViewModel by sharedStateViewModel()
    private val callbackManager = CallbackManager.Factory.create()

    /*line*/
    private val scopeList = listOf(Scope.PROFILE, Scope.OPENID_CONNECT)
    private val lineLoginResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            loginViewModel.processLoginIntent(
                activityResult.resultCode,
                activityResult.data
            )
        }

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            fadeIn()
        }
    }

    override fun setUp() {
        binding.apply {

        }
        loginViewModel.setIcon(0)
        listener()

        //init facebook
        initFacebookLogin()
    }

    override fun listener() {
        with(binding) {
            layoutLogin1.apply {
                btnLoginApple.apply {
                    setOnTouchAnimation()
                    setOnClickListener { }
                }


                btnLoginFacebook.apply {
                    setOnTouchAnimation()
                    setOnClickListener {
                        LoginManager.getInstance().logInWithReadPermissions(requireActivity(), listOf("public_profile"));
                        loginViewModel.selectedLoginType = SocialLoginTypeEnum.FB

                        //TermRegisterFragment.navigate(this@LoginFragment)

                    }
                }
                btnLoginLine.apply {
                    setOnTouchAnimation()
                    setOnClickListener {
                        val intent = loginViewModel.createLineLoginIntent(
                            context,
                            getString(R.string.line_channel_id),
                            scopeList
                        )
                        lineLoginResultLauncher.launch(intent)
                        loginViewModel.selectedLoginType = SocialLoginTypeEnum.LINE

                        //TermRegisterFragment.navigate(this@LoginFragment)
                    }
                }

                btnLogin.apply {
                    setOnTouchAnimation()
                    setOnClickListener {
                        goToInputPhoneLogin()
                    }
                }

                tvRegister.setOnTouchAnimation()
            }

            /*login user/pass*/
            layoutLogin2.apply {
                tvBackToLogin.apply {
                    setOnTouchAnimation()
                    setOnClickListener {
                        goToMainLogin()
                    }
                }
                tvForgotPassword.setOnTouchAnimation()

                edtPassword.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun afterTextChanged(p0: Editable?) {
                        val userName = edtPhoneNumber.text.toString().trim()
                        val pw = edtPassword.text.toString().trim()

                        if (userName.isNotEmpty() && pw.isNotEmpty()) {
                            isLoginFail = false
                        }

                    }
                })

                btnLoginApp.setOnTouchAnimation()
                btnLoginApp.setOnClickListener {
                    if (isValidateLoginForm()) {
                        loginViewModel.login(
                            LoginRequestModel(
                                mobile = edtPhoneNumber.text.toString(),
                                password = edtPassword.text.toString()
                            )
                        )
                    }
                }

            }
        }
    }

    override fun subscribe() {
        super.subscribe()

        loginViewModel.loginLiveData.observe(viewLifecycleOwner) {
            progressDialog.dismiss()
            when (it) {
                is ResultWrapper.Loading -> {
                    progressDialog.show(childFragmentManager)
                }

                is ResultWrapper.Success -> {
                    AppNavigator(requireActivity()).goToMain()
                }

                is ResultWrapper.GenericError -> {
                    //AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                    binding.layoutLogin2.isLoginFail = true
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                else -> {
                    /*none*/
                }
            }
        }

        loginViewModel.loginSocialLiveData.observe(viewLifecycleOwner) {
            progressDialog.dismiss()
            when (it) {
                is ResultWrapper.Loading -> {
                    showLoading()
                }

                is ResultWrapper.Success -> {
                    hideLoading()
                    if (it.response.data?.isFirstLogin == true) {
                        TermRegisterFragment.navigate(this)
                    } else {
                        AppNavigator(requireActivity()).goToMain()
                    }
                }

                is ResultWrapper.GenericError -> {
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                    hideLoading()
                }

                is ResultWrapper.NetworkError -> {
                    AppAlert.alert(requireContext(), it.message).show(childFragmentManager)
                }

                else -> {
                    /*none*/
                }
            }
        }

        /*lifecycleScope.launchWhenCreated {
            loginViewModel.userProfileFlow.collectLatest { user ->
                Log.d("LOGD", "subscribe line: $user")
            }
        }*/

    }

    private fun fadeIn() {
        with(binding) {
            flLogin.slideUp()
        }
    }

    private fun setLoginMain(isShow: Boolean) {
        binding.apply {
            layoutLogin1.llLoginMain.isVisible = !isShow
            layoutLogin2.llLoginApp.isVisible = isShow

            if (isShow) layoutLogin2.llLoginApp.slideUp()
        }
    }

    private fun isValidateLoginForm(): Boolean {
        var isValidate = true
        with(binding.layoutLogin2) {
            val userName = edtPhoneNumber.text.toString().trim()
            val pw = edtPassword.text.toString().trim()

            when {
                userName.isEmpty() -> {
                    isValidate = false
                    isLoginFail = true
                }

                pw.isEmpty() -> {
                    isValidate = false
                    isLoginFail = true
                }

                else -> {
                    isLoginFail = false
                }
            }
        }

        return isValidate
    }

    private fun initFacebookLogin() {
        FacebookSdk.setAutoInitEnabled(true)
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
                AppAlert.alert(requireContext(), error.message).show(childFragmentManager)
            }

            override fun onSuccess(result: LoginResult) {
                val token = result.accessToken.token
                Timber.d("$token")
                loginViewModel.loginSocial(
                    LoginSocialRequestModel(
                        social = SocialLoginTypeEnum.FB.name,
                        accessToken = token
                    )
                )
            }

        })
    }

    private fun getCurrentStep(): Int = if (binding.layoutLogin1.llLoginMain.isVisible) {
        1
    } else {
        2
    }

    override fun onBackPressed(): Boolean = if (getCurrentStep() == 2) {
        goToMainLogin()
        true
    } else {
        false
    }

    private fun goToMainLogin() {
        setLoginMain(false)
    }

    private fun goToInputPhoneLogin() {
        setLoginMain(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // add this line
        callbackManager.onActivityResult(
            requestCode,
            resultCode,
            data
        );
        super.onActivityResult(requestCode, resultCode, data)
    }

}
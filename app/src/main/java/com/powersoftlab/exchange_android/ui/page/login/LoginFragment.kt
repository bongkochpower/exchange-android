package com.powersoftlab.exchange_android.ui.page.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.alert.AppAlert
import com.powersoftlab.exchange_android.common.navigator.AppNavigator
import com.powersoftlab.exchange_android.databinding.FragmentLoginBinding
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ext.slideUp
import com.powersoftlab.exchange_android.model.body.LoginRequestModel
import com.powersoftlab.exchange_android.network.ResultWrapper
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Arrays

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by sharedViewModel()

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val callbackManager = CallbackManager.Factory.create()

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
                    }
                }
                btnLoginLine.apply {
                    setOnTouchAnimation()
                    setOnClickListener { }
                }
                btnLogin.apply {
                    setOnTouchAnimation()
                    setOnClickListener {
                        setLoginMain(true)
                    }
                }




                tvRegister.setOnTouchAnimation()
            }

            /*login user/pass*/
            layoutLogin2.apply {
                tvBackToLogin.apply {
                    setOnTouchAnimation()
                    setOnClickListener {
                        setLoginMain(false)
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

    private fun initFacebookLogin(){
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
                Log.d("LOGD", "onError: ${error.message}")
            }

            override fun onSuccess(result: LoginResult) {
                Log.d("LOGD", "onSuccess: $result")
            }

        })
    }

}
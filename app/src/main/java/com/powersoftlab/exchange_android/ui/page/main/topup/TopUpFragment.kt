package com.powersoftlab.exchange_android.ui.page.main.topup

import androidx.core.widget.doAfterTextChanged
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.enum.AuthByEnum
import com.powersoftlab.exchange_android.databinding.FragmentTopupBinding
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setTextSlideButtonEnable
import com.powersoftlab.exchange_android.ext.showKeyboard
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.withdraw.auth_with_bio.AuthWithBioFragment

class TopUpFragment : BaseFragment<FragmentTopupBinding>(R.layout.fragment_topup), OnBackPressedFragment  {

    override fun setUp() {
        showKeyboard()
        with(binding) {
            isTopupInvalid = false
        }
    }

    override fun listener() {
        with(binding) {

            edtMoneyAmount.doAfterTextChanged {
                if (isTopupInvalid == true) {
                    isTopupInvalid = false
                }
                slideToConfirm.setTextSlideButtonEnable(it.toString().isNotEmpty(),R.string.button_slide_to_topup)
            }
            /*npTopup.setOnNumberClickListener { type, number ->
                when (type) {
                    NumberPad.NumberPadEnum.NUMBER -> {
                        edtMoneyAmount.append(number)
                    }

                    NumberPad.NumberPadEnum.DELETE -> {
                        edtMoneyAmount.deleteAmount()
                    }

                    NumberPad.NumberPadEnum.DOT -> {
                        edtMoneyAmount.append(".")
                    }
                }
            }*/


            slideToConfirm.setTextSlideButtonEnable(false,R.string.button_slide_to_topup)
            slideToConfirm.setOnClickListener {
                if (!isMonoClickable()) return@setOnClickListener
                monoLastTimeClick()

                slideToConfirm.setEnable(false)
                slideToConfirm.setBackgroundRes(R.drawable.bg_slide_confirm_done)

                if(isValidate()){
                    val amount = edtMoneyAmount.text.toString().replace(",", "").toDoubleOrNull() ?: 0.0
                    gotoAuthBio(amount)
                }else{
                    slideToConfirm.setTextSlideButtonEnable(true,R.string.button_slide_to_topup)
                }

            }


        }
    }

    override fun subscribe() {
        super.subscribe()
    }

    private fun isValidate(): Boolean {
        var isValidate = false
        with(binding) {
            val inputMoney = edtMoneyAmount.text.toString()
            when {
                inputMoney.isEmpty() || inputMoney == "0" -> {
                    isTopupInvalid = true
                }

                else -> isValidate = true
            }
        }
        return isValidate
    }

    private fun gotoAuthBio(amount : Double) {
        val action = TopUpFragmentDirections.actionTopUpFragmentToAuthWithBioFragment2(AuthByEnum.TOPUP, amount.toFloat())
        AuthWithBioFragment.navigate(this@TopUpFragment,action)
    }

    override fun onBackPressed(): Boolean = false
}
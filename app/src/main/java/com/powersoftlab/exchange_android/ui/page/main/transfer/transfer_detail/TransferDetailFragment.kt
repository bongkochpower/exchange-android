package com.powersoftlab.exchange_android.ui.page.main.transfer.transfer_detail

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.enum.AuthByEnum
import com.powersoftlab.exchange_android.databinding.FragmentTransferDetailBinding
import com.powersoftlab.exchange_android.ext.isMonoClickable
import com.powersoftlab.exchange_android.ext.monoLastTimeClick
import com.powersoftlab.exchange_android.ext.setTextSlideButtonEnable
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.transfer.TransferFragmentDirections
import com.powersoftlab.exchange_android.ui.page.main.transfer.TransferViewModel
import com.powersoftlab.exchange_android.ui.page.main.withdraw.auth_with_bio.AuthWithBioFragment
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class TransferDetailFragment : BaseFragment<FragmentTransferDetailBinding>(R.layout.fragment_transfer_detail),OnBackPressedFragment {

    private val transferViewModel: TransferViewModel by sharedStateViewModel()

    companion object {
        fun newInstance() = TransferDetailFragment()
        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(TransferFragmentDirections.actionTransferFragmentToTransferDetailFragment())
    }

    override fun setUp() {
    }

    override fun listener() {
        binding.apply {


            slideToTransfer.setTextSlideButtonEnable(true,R.string.button_slide_to_transfer)
            slideToTransfer.setOnClickListener {
                if (!isMonoClickable()) return@setOnClickListener
                monoLastTimeClick()

                slideToTransfer.setEnable(false)
                slideToTransfer.setBackgroundRes(R.drawable.bg_slide_confirm_done)

                gotoAuthBio()
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    private fun gotoAuthBio() {
        //val amount = binding.edtMoneyAmount.text.toString().replace(",", "").toDoubleOrNull() ?: 0.0
        val action = TransferDetailFragmentDirections.actionTransferDetailFragmentToAuthWithBioFragmentTransfer(AuthByEnum.TRANSFER,0.toFloat())
        AuthWithBioFragment.navigate(this@TransferDetailFragment,action)
    }



}
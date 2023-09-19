package com.powersoftlab.exchange_android.ui.page.main.withdraw.withdraw_summary

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.FragmentWithdrawSummaryBinding
import com.powersoftlab.exchange_android.ext.createBitmapFromView
import com.powersoftlab.exchange_android.ext.saveBitmap
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.main.withdraw.WithdrawViewModel
import com.powersoftlab.exchange_android.ui.page.main.withdraw.auth_with_bio.AuthWithBioFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel


class WithDrawSummaryFragment : BaseFragment<FragmentWithdrawSummaryBinding>(R.layout.fragment_withdraw_summary){
    private val withdrawViewModel : WithdrawViewModel by sharedStateViewModel()
    private val DELAY = 1500L

    companion object{
        fun newInstance() = WithDrawSummaryFragment()
        fun navigate(fragment: Fragment) =
            fragment.findNavController().navigate(AuthWithBioFragmentDirections.actionAuthWithBioFragmentToWithDrawSummaryFragment())

    }

    override fun setUp() {
        withdrawViewModel.setIcon(0)
    }

    override fun listener() {
        with(binding){
            btnSavePicture.setOnClickListener {
                saveImageBitmap()
            }
        }

    }

    private fun saveImageBitmap(){
        binding.apply {
            lifecycleScope.launch(Dispatchers.IO) {
                progressDialog.show(childFragmentManager)
                layoutSummaryForSave.rootView.createBitmapFromView {
                    it.saveBitmap(requireContext())
                }
                delay(DELAY)
                progressDialog.dismiss()
                saveImageSuccess()
            }

        }


    }
    
    private fun saveImageSuccess(){
        val msg = resources.getString(R.string.message_save_picture_success)
        val btn = resources.getString(R.string.button_back_to_main)
        showAlertSuccessDialog(title = msg, textButtonRight = btn) {
            activity?.finish()
        }
    }

}
package com.powersoftlab.exchange_android.ui.page.main.transfer.transfer_summary

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.FragmentTransferSummaryBinding
import com.powersoftlab.exchange_android.ext.createBitmapFromView
import com.powersoftlab.exchange_android.ext.saveBitmap
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ui.page.base.BaseFragment
import com.powersoftlab.exchange_android.ui.page.base.OnBackPressedFragment
import com.powersoftlab.exchange_android.ui.page.main.transfer.TransferViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class TransferSummaryFragment : BaseFragment<FragmentTransferSummaryBinding>(R.layout.fragment_transfer_summary),OnBackPressedFragment {

    private val transferViewModel: TransferViewModel by sharedStateViewModel()
    private val DELAY = 1500L

    companion object {
        fun newInstance() = TransferSummaryFragment()
        fun navigate(fragment: Fragment,action : Int) = fragment.findNavController().navigate(action)
    }

    override fun setUp() {
        transferViewModel.setIcon(0)
        transferViewModel.setTitle("")
        setupSummaryData()
    }

    override fun listener() {
        binding.apply {
            btnSavePicture.apply {
                setOnTouchAnimation()
                setOnClickListener {
                    saveImageBitmap()
                }
            }
            btnBack.apply {
                setOnTouchAnimation()
                setOnClickListener{
                    activity?.finish()
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    private fun setupSummaryData() {
        /*val summaryData = args.response
        binding.apply {
            layoutSummary.item = summaryData
            layoutSummaryForSave.item = summaryData

            executePendingBindings()
        }*/

    }

    private fun saveImageBitmap(){
        binding.apply {
            lifecycleScope.launch(Dispatchers.IO) {
                progressDialog.show(childFragmentManager)
                layoutSummary.rootView.createBitmapFromView {
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
package com.powersoftlab.exchange_android.ui.page.main.transfer.transfer_summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.constant.AppConstant
import com.powersoftlab.exchange_android.databinding.FragmentTransferSummaryBinding
import com.powersoftlab.exchange_android.ext.convertUtcToIct
import com.powersoftlab.exchange_android.ext.createBitmapFromView
import com.powersoftlab.exchange_android.ext.parcelable
import com.powersoftlab.exchange_android.ext.reDateFormat
import com.powersoftlab.exchange_android.ext.saveBitmap
import com.powersoftlab.exchange_android.ext.setOnTouchAnimation
import com.powersoftlab.exchange_android.ext.toDashWhenNullOrEmpty
import com.powersoftlab.exchange_android.ext.toStringFormat
import com.powersoftlab.exchange_android.model.response.TransferResponseModel
import com.powersoftlab.exchange_android.model.response.WalletDetailResponseModel
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
    private var mTransferResponseModel : TransferResponseModel? = null

    companion object {
        const val KEY_TRANSFER_SUMMARY = "transfer_summary"

        fun newInstance() = TransferSummaryFragment()
        fun navigate(fragment: Fragment,action : Int,bundle: Bundle) = fragment.findNavController().navigate(action,bundle)
    }

    override fun getExtra(bundle: Bundle) {
        super.getExtra(bundle)

        mTransferResponseModel = bundle.parcelable(KEY_TRANSFER_SUMMARY)
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
        val wallet : WalletDetailResponseModel = transferViewModel.walletProfile ?: WalletDetailResponseModel()
        val transferResp = mTransferResponseModel

        //16 Oct 2023/17:52
        val dateTime = transferResp?.date.convertUtcToIct()
        val date = dateTime.reDateFormat(AppConstant.FORMAT_SERVICE_DATE,AppConstant.FORMAT_UI_DATE_ABB_MONTH).toDashWhenNullOrEmpty()
        val time = dateTime.reDateFormat(AppConstant.FORMAT_SERVICE_DATE_TIME, AppConstant.FORMAT_UI_TIME)

        val summaryData = TransferResponseModel.TransferSummaryModel(
            profileImage = wallet.profileImage,
            fullName = wallet.fullName,
            accountNumber = wallet.walletId,
            amount = transferViewModel.inputMoneyTransfer?.toStringFormat(),
            currency = transferViewModel.selectedCurrency?.currencyName,
            transactionId = transferResp?.transactionId,
            date = "$date/$time"

        )
        binding.apply {
            layoutSummary.transfer = summaryData
            layoutSummaryForSave.transfer = summaryData

            executePendingBindings()
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
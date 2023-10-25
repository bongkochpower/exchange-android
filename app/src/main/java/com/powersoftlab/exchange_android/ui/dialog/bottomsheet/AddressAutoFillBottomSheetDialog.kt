package com.powersoftlab.exchange_android.ui.dialog.bottomsheet

import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.BottomSheetDialogAddressAutoFillBinding
import com.powersoftlab.exchange_android.databinding.ItemRvAddressAutoFillBinding
import com.powersoftlab.exchange_android.model.response.AddressAutoFillResponseModel
import com.powersoftlab.exchange_android.ui.dialog.bottomsheet.base.BaseBottomSheetDialogFragment
import com.powersoftlab.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter

class AddressAutoFillBottomSheetDialog :
    BaseBottomSheetDialogFragment<BottomSheetDialogAddressAutoFillBinding>(R.layout.bottom_sheet_dialog_address_auto_fill) {

    private var list: MutableList<AddressAutoFillResponseModel.SubDistrictResponse>? = null

    //private var listSearch: MutableList<AddressAutoFillResponseModel.SubDistrictResponse>? = null
    private var callBackOnItemSelected: ((Int) -> Unit)? = null

    private val menuAdapter by lazy {
        SimpleRecyclerViewAdapter<AddressAutoFillResponseModel.SubDistrictResponse, ItemRvAddressAutoFillBinding>(
            R.layout.item_rv_address_auto_fill
        )
    }

    companion object {
        private const val KEY_ITEM = "key_item"

        fun newInstance(list: MutableList<AddressAutoFillResponseModel.SubDistrictResponse>): AddressAutoFillBottomSheetDialog =
            AddressAutoFillBottomSheetDialog().apply {
                val bundle = Bundle().apply {
                    putString(KEY_ITEM, Gson().toJson(list))
                }
                arguments = bundle
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CurveBottomSheetDialogTheme)
    }

    override fun getExtra(bundle: Bundle) {
        super.getExtra(bundle)
        with(bundle) {
            val collectionType = object : TypeToken<MutableList<AddressAutoFillResponseModel.SubDistrictResponse>?>() {}.type
            list = Gson().fromJson(getString(KEY_ITEM), collectionType)
        }
    }

    override fun setUp() {
        with(binding) {

            btnCancel.setOnClickListener { dismiss() }

            edtSearch.doAfterTextChanged {
                val str = it.toString()
                search(str)
            }

            recyclerView.apply {
                adapter = menuAdapter
            }
        }

        menuAdapter.onBindView { binding, item, position ->
            item?.let {
                with(binding) {
                    tvMenu.text = "${item.nameEN} - ${item.zipCode}"

                    root.setOnClickListener {
                        callBackOnItemSelected?.invoke(item.id ?: 0)
                        dismiss()
                    }
                }
            }
        }

        if (menuAdapter.itemCount == 0) {
            list?.let { menuAdapter.submitList(true, it) }
        }
    }

    fun setOnItemSelectedListener(listener: ((Int) -> Unit)) {
        callBackOnItemSelected = listener
    }

    private fun search(str: String) {
        if (str.isNotEmpty()) {
            val searchList = list?.filter { it.zipCode?.contains(str) == true || it.nameEN?.contains(str) == true }?.toMutableList() ?: mutableListOf()
            menuAdapter.updateList(searchList, true)
        } else {
            list?.let { menuAdapter.submitList(true, it) }
        }
    }
}

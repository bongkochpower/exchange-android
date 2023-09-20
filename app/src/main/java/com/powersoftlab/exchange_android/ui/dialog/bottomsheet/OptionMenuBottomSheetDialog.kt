package com.powersoftlab.exchange_android.ui.dialog.bottomsheet

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.BottomSheetDialogOptionMenuBinding
import com.powersoftlab.exchange_android.databinding.ItemRvOptionMenuBinding
import com.powersoftlab.exchange_android.ui.dialog.bottomsheet.base.BaseBottomSheetDialogFragment
import com.powersoftlab.exchange_android.ui.list.adapter.SimpleRecyclerViewAdapter

class OptionMenuBottomSheetDialog<T> :
    BaseBottomSheetDialogFragment<BottomSheetDialogOptionMenuBinding>(R.layout.bottom_sheet_dialog_option_menu) {

    private var list: MutableList<OptionMenuModel<T>>? = null

    private var callBackOnItemSelected: ((OptionMenuModel<T>) -> Unit)? = null

    private val menuAdapter by lazy {
        SimpleRecyclerViewAdapter<OptionMenuModel<T>, ItemRvOptionMenuBinding>(
            R.layout.item_rv_option_menu
        )
    }

    companion object {
        private const val KEY_ITEM = "key_item"

        fun <T> newInstance(list: MutableList<OptionMenuModel<T>>): OptionMenuBottomSheetDialog<T> =
            OptionMenuBottomSheetDialog<T>().apply {
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
            val collectionType = object : TypeToken<MutableList<OptionMenuModel<T>>?>() {}.type
            list = Gson().fromJson(getString(KEY_ITEM), collectionType)
        }
    }

    override fun setUp() {
        with(binding) {

            btnCancel.setOnClickListener { dismiss() }

            recyclerView.apply {
                adapter = menuAdapter
            }
        }

        menuAdapter.onBindView { binding, item, position ->
            item?.let {
                with(binding) {
                    tvMenu.text = item.name

                    root.setOnClickListener {
                        callBackOnItemSelected?.invoke(item)
                        dismiss()
                    }
                }
            }
        }

        if (menuAdapter.itemCount == 0) {
            list?.let { menuAdapter.submitList(true, it) }
        }
    }

    fun setOnItemSelectedListener(listener: ((OptionMenuModel<T>) -> Unit)) {
        callBackOnItemSelected = listener
    }
}

data class OptionMenuModel<T>(
    var name: String? = null,
    var data: T? = null
)

fun <D, CZ> OptionMenuModel<D>.convertToModel(clazz: Class<CZ>): CZ {
    val jsonObject = Gson().toJsonTree(this.data).asJsonObject
    return Gson().fromJson(jsonObject, clazz)
}
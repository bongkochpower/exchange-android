//package com.feyverly.hipowershot.ui.dialog.popup.base
//
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import android.view.*
//import androidx.databinding.DataBindingUtil
//import androidx.databinding.ViewDataBinding
//import androidx.fragment.app.FragmentManager
//import com.startwithn.exchange_android.common.manager.AppManager
//import com.startwithn.exchange_android.ext.fixFontScale
//import com.startwithn.exchange_android.ext.hideKeyboard
//import com.startwithn.exchange_android.ext.overrideUIText
//import com.startwithn.exchange_android.ui.dialog.popup.ProgressDialog
//import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment
//import org.koin.android.ext.android.inject
//import org.koin.java.KoinJavaComponent.inject
//
//abstract class BlurDialogFragment<B : ViewDataBinding>(private var isCanBack: Boolean = false) : SupportBlurDialogFragment() {
//
//    protected val appManager: AppManager by inject()
//    protected lateinit var binding: B
//    var isShow: Boolean = false
//    protected val progressDialog: ProgressDialog by lazy { ProgressDialog.newInstance() }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        arguments?.let {
//            getExtra(it)
//        }
//
//        context?.apply {
//            fixFontScale()
//            overrideUIText(binding.root)
//        }
//
//        setUp()
//
//        subscribe()
//
//        hideKeyboard()
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        dialog.let {
//            it?.window?.setLayout(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//            it?.setCancelable(isCanBack)
//        }
//    }
//
//    abstract fun getLayoutId(): Int
//
//    open fun getExtra(bundle: Bundle) {}
//
//    open fun setUp() {}
//
//    open fun subscribe() {}
//
//    override fun show(manager: FragmentManager, tag: String?) {
//        super.show(manager, tag)
//        isShow = true
//    }
//
//    override fun dismiss() {
//        try {
//            if (isShow) {
//                super.dismiss()
//                isShow = false
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    /*blur config*/
//    override fun getDownScaleFactor(): Float {
////        return super.getDownScaleFactor()
//        return 24f
//    }
//}
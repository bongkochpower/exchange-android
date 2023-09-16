package com.powersoftlab.exchange_android.ui.page.base

import android.content.res.Configuration
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.powersoftlab.exchange_android.common.databinding.inflater.contentView
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.ext.fixFontScale
import com.powersoftlab.exchange_android.ext.getNavigationBarHeight
import com.powersoftlab.exchange_android.ext.goToGoogleStore
import com.powersoftlab.exchange_android.ext.hideKeyboard
import com.powersoftlab.exchange_android.ext.logout
import com.powersoftlab.exchange_android.ui.dialog.popup.AlertMessageDialog
import com.powersoftlab.exchange_android.common.enum.AppEventEnum
import com.powersoftlab.exchange_android.common.rx.RxBus
import com.powersoftlab.exchange_android.common.rx.RxEvent
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.ui.dialog.popup.AlertSuccessDialog
import com.powersoftlab.exchange_android.ui.dialog.popup.ProgressDialog
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

abstract class BaseActivity<B : ViewDataBinding>(@LayoutRes private val layout: Int) :
    AppCompatActivity() {

    protected val TAG = javaClass.name

    protected val appManager: AppManager by inject()

    protected val binding: B by contentView(layout)

    protected val progressDialog: ProgressDialog by lazy { ProgressDialog.newInstance() }

    private var alertMessageDialog: AlertMessageDialog? = null
    private var alertSuccessDialog: AlertSuccessDialog? = null

    /*for rx bus listener*/
    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }
    private var isShowEventAppDialog = false

    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.root.requestLayout()

        intent.extras?.let {
            getExtra(it)
        }

        this.apply {
            fixFontScale()
//            overrideUIText(binding.root)
        }

        setUp()
        listener()

        subscribe()

        //hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    open fun getExtra(bundle: Bundle) {}

    abstract fun setUp()
    abstract fun listener()

    open fun subscribe() {
        handleAppEvent()
    }

    fun showLoading(){
        if(progressDialog.isShow) return
        progressDialog.show(supportFragmentManager)
    }
    fun hideLoading(){
        progressDialog.dismissAllowingStateLoss()
    }

    private fun handleAppEvent() {
        disposable.add(RxBus.listen(RxEvent.AppEvent::class.java).subscribe {
            runOnUiThread {
                try {
                    when (it.event) {
                        AppEventEnum.MAINTENANCE -> showMaintenance()
                        AppEventEnum.SOFT_UPDATE -> showSoftUpdate(it.message)
                        AppEventEnum.FORCE_UPDATE -> showForceUpdate()
                        AppEventEnum.UNAUTHORIZED -> showUnAuthorized(it.message)
                        else -> {
                            /*none*/
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun showMaintenance() {
        if (isCanShowEventPopup()) {
            isShowEventAppDialog = true
            showAlertMessage(message = getString(R.string.message_maintenance)) {
                isShowEventAppDialog = false
                finishAffinity()
            }
        }
    }

    private fun showSoftUpdate(version: String?) {
        if (isCanShowEventPopup()) {
            isShowEventAppDialog = true
            val message =
                "${getString(R.string.app_name)} $version ${getString(R.string.message_soft_update)}"
            showAlertMessage(
                message = message,
                textButtonLeft = getString(R.string.button_cancel),
                textButtonRight = getString(R.string.button_update),
                onLeftClick = {
                    isShowEventAppDialog = false
                },
                onRightClick = {
                    isShowEventAppDialog = false
                    goToGoogleStore(this)
                })
        }
    }

    private fun showForceUpdate() {
        if (isCanShowEventPopup()) {
            isShowEventAppDialog = true
            val message =
                "${getString(R.string.message_force_update)} ${getString(R.string.app_name)}"
            showAlertMessage(message = message, textButton = getString(R.string.button_update)) {
                isShowEventAppDialog = false
                finishAffinity()
                goToGoogleStore(this)
            }
        }
    }

    private fun showUnAuthorized(message: String?) {
        if (isCanShowEventPopup()) {
            isShowEventAppDialog = true
            showAlertMessage(message = message, textButton = getString(R.string.button_logout)) {
                isShowEventAppDialog = false
                logout()
            }
        }
    }

    private fun isCanShowEventPopup(): Boolean = !isShowEventAppDialog && !isFinishing

    fun showAlertMessage(
        title: String? = null,
        message: String? = null,
        isMessageAlignLeft: Boolean = false,
        textButton: String? = getString(R.string.button_close),
        onClick: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null
    ) {
        alertMessageDialog?.dismiss()
        alertMessageDialog =
            AlertMessageDialog.newInstance(
                title = title,
                message = message,
                textButtonRight = textButton,
                isSingleChoice = true,
                isMessageLeft = isMessageAlignLeft
            )
        alertMessageDialog?.apply {
            setOnButtonRightClick {
                onClick?.invoke()
                it.dismiss()
            }
            setOnButtonLeftClick {
                onClick?.invoke()
                it.dismiss()
            }
            setOnDismissListener {
                onDismiss?.invoke()
            }

            show(supportFragmentManager)
        }
    }

    fun showAlertMessage(
        title: String? = null,
        message: String? = null,
        isMessageAlignLeft: Boolean = false,
        textButtonLeft: String? = null,
        textButtonRight: String? = null,
        onLeftClick: (() -> Unit)? = null,
        onRightClick: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null
    ) {
        alertMessageDialog?.dismiss()
        alertMessageDialog =
            AlertMessageDialog.newInstance(
                title = title,
                message = message,
                textButtonLeft = textButtonLeft,
                textButtonRight = textButtonRight,
                isMessageLeft = isMessageAlignLeft
            )
        alertMessageDialog?.apply {
            setOnButtonLeftClick {
                onLeftClick?.invoke()
                it.dismiss()
            }
            setOnButtonRightClick {
                onRightClick?.invoke()
                it.dismiss()
            }
            setOnDismissListener {
                onDismiss?.invoke()
            }

            show(supportFragmentManager)
        }
    }

    fun showAlertSuccessDialog(
        title: String? = null,
        textButtonRight: String? = null,
        onRightClick: (() -> Unit)? = null
    ) {
        alertSuccessDialog?.dismiss()
        alertSuccessDialog = AlertSuccessDialog.newInstance(
            title = title,
            textButtonRight = textButtonRight
        )
        alertSuccessDialog?.apply {
            setOnButtonConfirmClick {
                onRightClick?.invoke()
                it.dismiss()
            }
            show(supportFragmentManager)
        }
    }

    fun initFullScreenWithStatusBar(isAddMargin: Boolean = true) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        // Add Margin
        if (isAddMargin) {
            with(binding) {
                root.layoutParams?.let { layoutParams ->
                    if (layoutParams is ViewGroup.MarginLayoutParams) {
                        when (resources.configuration.orientation) {
                            Configuration.ORIENTATION_PORTRAIT -> {
                                layoutParams.bottomMargin = getNavigationBarHeight()
                            }

                            else -> {
                                /*none*/
                            }
                        }
                    }
                }
            }
        }
    }


}
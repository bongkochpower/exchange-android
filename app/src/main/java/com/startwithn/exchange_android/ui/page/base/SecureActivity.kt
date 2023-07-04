package com.startwithn.exchange_android.ui.page.base

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

abstract class SecureActivity<B : ViewDataBinding>(@LayoutRes layout:Int) : BaseActivity<B>(layout) {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        super.onCreate(savedInstanceState)
    }
}
package com.startwithn.exchange_android.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.databinding.DataBindingUtil
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.WidgetAppToolbarBinding

class AppToolbar(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private val binding: WidgetAppToolbarBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.widget_app_toolbar,
        null,
        false
    )

    private var callBack: (() -> Unit)? = null

    init {
        addView(
            binding.root,
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.AppToolbar)

        binding.apply {
            widget = this@AppToolbar
            title = attributes.getString(R.styleable.AppToolbar_title)
        }

        attributes.recycle()

    }

    fun setIcon(@DrawableRes resourceID: Int) {
        binding.btnClose.setImageResource(resourceID)
    }

    fun setOnBackListener(cb: () -> Unit) {
        callBack = cb
    }

    fun onBackPressed(){
        callBack?.invoke()
    }
}
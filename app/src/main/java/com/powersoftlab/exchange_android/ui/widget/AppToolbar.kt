package com.powersoftlab.exchange_android.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.databinding.DataBindingUtil
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.WidgetAppToolbarBinding
import com.powersoftlab.exchange_android.ext.gone

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
        val iconBackResId = attributes.getResourceId(R.styleable.AppToolbar_iconBackSrc, 0)
        val title =  attributes.getString(R.styleable.AppToolbar_title)

        binding.apply {
            widget = this@AppToolbar
            this.title = title
        }

        if(title?.isNotEmpty() == true){
            binding.logoApp.gone()
        }

        initView()

        setIcon(iconBackResId)
        attributes.recycle()

    }

    private fun initView(){
        binding.apply {
            btnClose.setOnClickListener {
                //(context as Activity).onBackPressed()
                callBack?.invoke()
            }
        }
    }

    fun setIcon(@DrawableRes resourceID: Int) {
        binding.btnClose.setImageResource(resourceID)
    }

    fun setOnBackListener(cb: () -> Unit) {
        callBack = cb
    }

    fun setTitle(title: String?) {
        with(binding) {
            tvTitle.text = title
        }
    }

}
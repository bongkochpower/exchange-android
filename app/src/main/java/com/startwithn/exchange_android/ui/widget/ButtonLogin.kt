package com.startwithn.exchange_android.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.databinding.DataBindingUtil
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.WidgetButtonLoginBinding

class ButtonLogin(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private val binding: WidgetButtonLoginBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.widget_button_login,
        this,
        true
    )

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ButtonLogin)
        val title = attributes.getString(R.styleable.ButtonLogin_buttonTitle)
        val iconResId = attributes.getResourceId(R.styleable.ButtonLogin_buttonIconSrc,0)
        val bgResId = attributes.getResourceId(R.styleable.ButtonLogin_buttonBgSrc,0)

        setTitle(title)
        setIcon(iconResId)
        setBackground(bgResId)

        attributes.recycle()

    }

    fun setTitle(title: String?) {
        with(binding) {
            tvTitle.text = title
        }
    }

    fun setIcon(@DrawableRes resId:Int){
        with(binding) {
            imgIcon.setImageResource(resId)
        }
    }
    fun setBackground(@DrawableRes resId:Int){
        with(binding) {
            imgBg.setImageResource(resId)
        }
    }
}
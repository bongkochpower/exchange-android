//package com.powersoftlab.exchange_android.ui.widget
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.LayoutInflater
//import android.widget.FrameLayout
//import androidx.annotation.DrawableRes
//import androidx.databinding.DataBindingUtil
//import com.powersoftlab.exchange_android.R
//import com.powersoftlab.exchange_android.databinding.WidgetAppToolbarBinding
//import com.powersoftlab.exchange_android.databinding.WidgetCircleMenuBinding
//import com.powersoftlab.exchange_android.ext.gone
//
//class CircleMenu(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
//    private val binding: WidgetCircleMenuBinding = DataBindingUtil.inflate(
//        LayoutInflater.from(context),
//        R.layout.widget_circle_menu,
//        null,
//        true
//    )
//
//    private var callBack: (() -> Unit)? = null
//
//    init {
//        addView(
//            binding.root,
//            LayoutParams.WRAP_CONTENT,
//            LayoutParams.WRAP_CONTENT
//        )
//
//        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleMenu)
//        val iconResId = attributes.getResourceId(R.styleable.CircleMenu_iconSrc, 0)
//        val title =  attributes.getString(R.styleable.CircleMenu_menuTitle)
//
//        initView()
//
//        setTitle(title)
//        setIcon(iconResId)
//
//        attributes.recycle()
//
//    }
//
//    private fun initView(){
//        binding.apply {
////            root.setOnClickListener {
////                //(context as Activity).onBackPressed()
////                callBack?.invoke()
////            }
//        }
//    }
//
//    fun setIcon(@DrawableRes resourceID: Int) {
//        binding.imgMenu.setImageResource(resourceID)
//    }
//
//
//    fun setTitle(title: String?) {
//        with(binding) {
//            tvText.text = title
//        }
//    }
//
//}

package com.powersoftlab.exchange_android.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.databinding.DataBindingUtil
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.WidgetButtonLoginBinding
import com.powersoftlab.exchange_android.databinding.WidgetCircleMenuBinding
import com.powersoftlab.exchange_android.ext.gone

class CircleMenu(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private val binding: WidgetCircleMenuBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.widget_circle_menu,
        this,
        true
    )

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleMenu)
        val title = attributes.getString(R.styleable.CircleMenu_menuTitle)
        val iconResId = attributes.getResourceId(R.styleable.CircleMenu_iconSrc,0)

        setTitle(title)
        setIcon(iconResId)

        attributes.recycle()

    }

    fun setTitle(title: String?) {
        with(binding) {
            tvText.text = title
        }
    }

    fun setIcon(@DrawableRes resId:Int){
        with(binding) {
            imgMenu.setImageResource(resId)
        }
    }
}
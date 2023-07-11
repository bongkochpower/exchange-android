package com.startwithn.exchange_android.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.databinding.WidgetNumberPadBinding

class NumberPad(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    enum class NumberPadEnum {
        NUMBER,
        DELETE,
        DOT
    }

    private var binding: WidgetNumberPadBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.widget_number_pad,
        this,
        true
    )

    private var callback: ((type: NumberPadEnum, number: String?) -> Unit)? = null

    init {

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.NumberPad)
        attributes.recycle()

        initView()

    }

    private fun initView() {
        with(binding) {
            btnOne.setOnClickListener { callback?.invoke(NumberPadEnum.NUMBER, 1.toString()) }
            btnTwo.setOnClickListener { callback?.invoke(NumberPadEnum.NUMBER, 2.toString()) }
            btnThree.setOnClickListener { callback?.invoke(NumberPadEnum.NUMBER, 3.toString()) }
            btnFour.setOnClickListener { callback?.invoke(NumberPadEnum.NUMBER, 4.toString()) }
            btnFive.setOnClickListener { callback?.invoke(NumberPadEnum.NUMBER, 5.toString()) }
            btnSix.setOnClickListener { callback?.invoke(NumberPadEnum.NUMBER, 6.toString()) }
            btnSeven.setOnClickListener { callback?.invoke(NumberPadEnum.NUMBER, 7.toString()) }
            btnEight.setOnClickListener { callback?.invoke(NumberPadEnum.NUMBER, 8.toString()) }
            btnNine.setOnClickListener { callback?.invoke(NumberPadEnum.NUMBER, 9.toString()) }
            btnZero.setOnClickListener { callback?.invoke(NumberPadEnum.NUMBER, 0.toString()) }

            btnDot.setOnClickListener {
                callback?.invoke(NumberPadEnum.DOT, null)
            }

            btnDelete.setOnClickListener {
                callback?.invoke(NumberPadEnum.DELETE, null)
            }

        }
    }

    fun setOnNumberClickListener(callback: ((type: NumberPadEnum, number: String?) -> Unit)?) {
        this.callback = callback
    }
}
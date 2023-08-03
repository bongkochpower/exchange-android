package com.powersoftlab.exchange_android.ui.widget

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.databinding.WidgetEdittextRegisterBinding

class EdittextRegister(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private val binding: WidgetEdittextRegisterBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.widget_edittext_register,
        this,
        true
    )

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.EdittextRegister)
        val title = attributes.getString(R.styleable.EdittextRegister_edtTitle)
        val isError = attributes.getBoolean(R.styleable.EdittextRegister_isValidateError,false)

        setTitle(title)
        setValidation(isError)

        attributes.recycle()

    }

    fun setTitle(title: String?) {
        with(binding) {
            edtRegister.hint = title
        }
    }

    fun getText() : String{
        return binding.edtRegister.text.toString().trim()
    }
    fun setText(text : String){
        binding.edtRegister.setText(text)
    }
    fun getHintText() : String{
        return binding.edtRegister.hint.toString().trim()
    }

    fun setValidation(isError : Boolean, msg : String = ""){
        with(binding) {
            isErrorValidate = isError
            tvErrorValidate.text = msg
        }
    }
    fun getValidation() = binding.isErrorValidate ?: true

    fun addOnTextChangeListener(textWatcher: TextWatcher){
        binding.edtRegister.addTextChangedListener(textWatcher)
    }
}
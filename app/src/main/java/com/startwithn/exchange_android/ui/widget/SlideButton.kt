package com.startwithn.exchange_android.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.startwithn.exchange_android.R


/**
 * Created by tanjiahao on 2018/10/23
 * Original Project HoSlideButton
 */
open class SlideButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var floatView: View
    private val floatViewLayoutParams: LayoutParams
    private val txtLayoutParams: LayoutParams

    private var textView: TextView

    private val radioAutoScroll = 0.66
    private val radius2 = 45f
    private var isEnable = true

    init {
        layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        isClickable = false

        txtLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER_VERTICAL
            marginStart = context.dp2px(radius2) - context.dp2px(8f)
        }
        textView = TextView(context)


        floatViewLayoutParams = LayoutParams(context.dp2px(radius2), context.dp2px(radius2)).apply {
            gravity = Gravity.CENTER_VERTICAL
            marginStart = context.dp2px(5f)
            marginEnd = context.dp2px(5f)
        }

    }

    fun initView(isEnable: Boolean) {
        //textview
        this.removeAllViews()
        this.isEnable = isEnable
        textView.gravity = Gravity.CENTER
        this.addView(textView, txtLayoutParams)

        //icon
        floatView = ImageView(context).apply {
            layoutParams = floatViewLayoutParams
            setImageResource(if (isEnable) R.drawable.button_slide_confirm else R.drawable.button_slide_disable)
        }
        this.addView(floatView)

        this.setBackgroundResource(if (isEnable) R.drawable.bg_slide_confirm else R.drawable.bg_slide_confirm_disable)
    }

    fun setText(text: CharSequence, isEnable: Boolean = false) {
        textView.text = text
        textView.alpha = 1f

        //primary slide
        setTextSize(21f)
        val color = if (isEnable) context.getColor(R.color.colorPrimary) else context.getColor(R.color.gray_silver_sand)
        setTextColor(color)
        //notifyTextAlpha(1)
        val typeface = ResourcesCompat.getFont(context, R.font.prompt_regular)
        typeface?.let { setTextStyle(it) }

    }

    fun setTextSize(size: Float) {
        textView.textSize = size
    }

    fun setTextSize(unit: Int, size: Float) {
        textView.setTextSize(unit, size)
    }

    fun setTextColor(@ColorInt resId: Int) {
        textView.setTextColor(resId)
    }

    fun setTextStyle(tf: Typeface) {
        textView.typeface = tf
    }

    fun setGravity(gravity: Int) {
        textView.gravity = gravity
    }

    fun setBackgroundRes(bgRes: Int) {
        this.setBackgroundResource(bgRes)
    }

    fun setEnable(isEnable: Boolean){
        this.isEnable = isEnable
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                if (isEnable) {
                    return super.dispatchTouchEvent(event)
                } else
                    return false
            }
        }

        return super.dispatchTouchEvent(event)
    }

    private var lastX: Float = 0f
    private var lastY: Float = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {

                val deltaX = event.x - lastX
                val deltaY = event.y - lastY

                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    //move
                    val distanceX = Math.max(
                        floatViewLayoutParams.marginStart - floatView.left,
                        Math.min(deltaX.toInt(), measuredWidth - floatView.right - floatViewLayoutParams.marginEnd)
                    )

                    val newFloatViewLeft = floatView.left + distanceX
                    floatView.layout(newFloatViewLeft, floatView.top, floatView.right + distanceX, floatView.bottom)
                    notifyTextAlpha(newFloatViewLeft)
                }
            }

            MotionEvent.ACTION_UP -> {
                if (event.x < measuredWidth * radioAutoScroll) {
                    autoScrollToStart(floatViewLayoutParams.marginStart - floatView.left.toFloat())
                } else {
                    autoScrollToEnd((measuredWidth - floatView.right - floatViewLayoutParams.marginEnd).toFloat())
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                autoScrollToStart(floatViewLayoutParams.marginStart - floatView.left.toFloat())
            }
        }
        lastX = event.x
        lastY = event.y

        return true
    }

    private fun notifyTextAlpha(floatViewLeft: Int) {
        textView.alpha = 1 - (floatViewLeft / measuredWidth.toFloat())
    }

    fun slideToStartPos() {
        autoScrollToStart(floatViewLayoutParams.marginStart - floatView.left.toFloat())
    }

    private fun autoScrollToEnd(deltaX: Float) {
        autoScroll(deltaX, true)
    }

    private fun autoScrollToStart(deltaX: Float) {
        autoScroll(deltaX, false)
    }

    private fun autoScroll(deltaX: Float, activated: Boolean) {
        val translateAnimation = TranslateAnimation(0f, deltaX, 0f, 0f)
        translateAnimation.duration = 100
        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                floatView.clearAnimation()

                val floatViewLeft = floatView.left + deltaX.toInt()
                floatView.layout(floatViewLeft, floatView.top, floatView.right + deltaX.toInt(), floatView.bottom)
                notifyTextAlpha(floatViewLeft)

                if (activated) {
                    callOnClick()
                }
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        floatView.startAnimation(translateAnimation)

    }


    fun Context.dp2px(dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}

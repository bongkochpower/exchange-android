package com.startwithn.exchange_android.ext

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.SystemClock
import android.text.Layout
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.startwithn.exchange_android.R
import com.startwithn.exchange_android.common.constant.AppConstant
import com.startwithn.exchange_android.ui.list.ItemViewType
import com.startwithn.exchange_android.ui.list.LoadingIVT
import com.startwithn.exchange_android.ui.list.itemdecoration.EqualSpacingItemDecoration
import java.io.File
import java.lang.reflect.Field
import kotlin.math.roundToInt

private var lastClickTime = 0L

/**check when use mono-click is clickable**/
fun Any.isMonoClickable(): Boolean {
    return (SystemClock.elapsedRealtime() - lastClickTime) >= 800
}

/**stamp(set) last time click**/
fun Any.monoLastTimeClick() {
    lastClickTime = SystemClock.elapsedRealtime()
}

fun Context.fixFontScale() {
    val configuration: Configuration = resources.configuration
    if (configuration.fontScale > 1) {
        configuration.fontScale = 1.toFloat()
        val metrics = resources.displayMetrics
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        createConfigurationContext(configuration)
    }
}

//fun Context.overrideUIText(root: View) {
//    AppManager(this).getUiContentsList()?.let { uiContentList ->
//        root.getAllChildren().forEach { v ->
//            v.tag?.let { tag ->
//                val viewTag = tag as String
//                uiContentList.find { it.code == viewTag }?.let { uiContentModel ->
//                    val content = uiContentModel.content
//                    if (!content.isNullOrEmpty()) {
//                        when (v) {
//                            is Button -> {
//                                v.text = content
//                            }
//                            is EditText -> {
//                                v.hint = content
//                            }
//                            is TextView -> {
//                                v.text = content
//                            }
//                            is CheckBox -> {
//                                v.text = content
//                            }
//                            is SearchView -> {
//                                v.queryHint = content
//                            }
////                        is AppToolbar -> {
//////                            v.setTitle(content)
////                        }
////                        is AppToolbarPaymentResult -> {
//////                            v.setTitle(content)
////                        }
////                        is AppToolbarProduct -> {
//////                            v.setTitle(content)
////                        }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

/*view*/
fun View.getAllChildren(): MutableList<View> {
    val result: MutableList<View> = ArrayList()
    if (this !is ViewGroup) {
        result.add(this)
        return result
    }
    val viewGroup = this
    for (i in 0 until viewGroup.childCount) {
        val child = viewGroup.getChildAt(i)
        val viewArrayList: MutableList<View> = ArrayList()
        viewArrayList.add(this)
        viewArrayList.addAll(child.getAllChildren())
        result.addAll(viewArrayList)
    }
    return result
}

fun View.fadeIn() {
    alpha = 0.5f
    animate().apply {
        interpolator = LinearInterpolator()
        duration = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
        alpha(1f)
        startDelay = 400
        start()
    }
//    startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
}
fun View.fadeOut() {
    alpha = 1f
    animate().apply {
        interpolator = LinearInterpolator()
        duration = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
        alpha(0.5f)
        startDelay = 400
        start()
    }
}

fun View.slideUp() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
}

fun View.slideDown() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_down))
}

/*for scale animation*/
const val DELAY_SCALE: Long = 120
const val SCALE_DOWN: Float = 0.95f
const val SCALE_DEFAULT: Float = 1.0f

fun View.startScaleDownAnimation() {
    val scaleDownX: ObjectAnimator = ObjectAnimator.ofFloat(this, "scaleX", SCALE_DOWN)
    val scaleDownY: ObjectAnimator = ObjectAnimator.ofFloat(this, "scaleY", SCALE_DOWN)
    scaleDownX.duration = DELAY_SCALE
    scaleDownY.duration = DELAY_SCALE
    scaleDownX.start()
    scaleDownY.start()
}

fun View.resetScaleAnimation() {
    val scaleDownX: ObjectAnimator = ObjectAnimator.ofFloat(this, "scaleX", SCALE_DEFAULT)
    val scaleDownY: ObjectAnimator = ObjectAnimator.ofFloat(this, "scaleY", SCALE_DEFAULT)
    scaleDownX.duration = DELAY_SCALE
    scaleDownY.duration = DELAY_SCALE
    scaleDownX.start()
    scaleDownY.start()
}

@SuppressLint("ClickableViewAccessibility")
fun View.setOnTouchAnimation() {
    this.setOnTouchListener { view, motionEvent ->
        try {
            when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> view.startScaleDownAnimation()
                MotionEvent.ACTION_UP -> view.resetScaleAnimation()
                MotionEvent.ACTION_CANCEL -> view.resetScaleAnimation()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        false
    }
}

/*text view*/
fun TextView.highlight(context: Context?, message: String?, @ColorRes colorRes: Int) {
    message?.let {
        val spannableString = SpannableString(text)

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {}
            override fun updateDrawState(textPaint: TextPaint) {
                super.updateDrawState(textPaint)
                context?.let { context1 ->
                    textPaint.isUnderlineText = false
                    textPaint.isFakeBoldText = true
                    textPaint.color = ContextCompat.getColor(context1, colorRes)
                }
            }
        }

        val textLength: Int = text.length

        spannableString.setSpan(
            clickableSpan,
            textLength - it.length,
            textLength,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        text = spannableString
    }
}

fun Any.addTextUnderlines(vararg textViews: TextView) {
    textViews.forEach {
        it.paintFlags = it.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }
}

@BindingAdapter(value = ["setUIDate"])
fun TextView.setUIDate(value: String?) {
    text = value.reDateFormat(AppConstant.FORMAT_SERVICE_DATE, AppConstant.FORMAT_UI_DATE)
}

fun TextView.isEllipsize(): Boolean {
    val layout: Layout = layout
    val lines: Int = layout.lineCount
    if (lines > 0) return layout.getEllipsisCount(lines - 1) > 0
    return false
}

fun TextView.removeMaxLines() {
    maxLines = Integer.MAX_VALUE
}


/*edit text*/
fun isActionDone(actionId: Int): Boolean =
    actionId == EditorInfo.IME_ACTION_DONE

fun EditText.removeStartWithSpace(){
    this.doAfterTextChanged {  editable ->
        val str : String = editable.toString()
        if (str.length == 1 && str.startsWith(" ")) {
            editable?.clear()
        }
        //remove on type finish after that input space
        if(this.editableText.startsWith(" ")){
            this.setText(this.editableText.removePrefix(" "))
        }
    }
}

/*scroll view*/
fun NestedScrollView.scrollToBottom() = run { postDelayed({ fullScroll(View.FOCUS_DOWN) }, 500) }

/*recyclerview*/
inline fun RecyclerView.setOnLoadMoreListener(crossinline isLoading: (() -> Boolean), crossinline loadMore: (() -> Unit)) {
    var userScrolled = false
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                userScrolled = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                if (!isLoading()) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if (userScrolled && visibleItemCount + pastVisibleItems == totalItemCount) {
                        userScrolled = false
                        loadMore.invoke()
                    }
                }
            }
        }
    })
}

//fun RecyclerView.hideSkeleton() {
//    if (this.tag is RecyclerViewSkeletonScreen) {
//        val sk = this.tag as RecyclerViewSkeletonScreen
//        sk.hide()
//        this.tag = null
//    }
//}
//
//fun RecyclerView.showSkeleton(@LayoutRes layoutResId: Int, itemCount: Int): SkeletonScreen {
//    val sk = Skeleton.bind(this)
//        .load(layoutResId)
//        .adapter(this.adapter)
//        .count(itemCount)
//        .frozen(false)
//        .color(R.color.color_sk)
//        .show()
//    this.tag = sk
//    return sk
//}
//
//fun RecyclerView.isShowSkeleton(): Boolean {
//    var isShow = false
//    if (this.tag is RecyclerViewSkeletonScreen) {
//        isShow = true
//    }
//    return isShow
//}

fun RecyclerView.setItemPadding(spacing: Float, displayMode: Int = -1) {
    if (itemDecorationCount == 0) {
        addItemDecoration(
            EqualSpacingItemDecoration(
                convertDpToPx(resources, spacing).toInt(),
                displayMode
            )
        )
    }
}

fun RecyclerView.addLinear() {
    if (itemDecorationCount == 0) {
        addItemDecoration(object : DividerItemDecoration(context, LinearLayoutManager.VERTICAL) {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                // hide the divider for the last child
                if (position == parent.adapter!!.itemCount - 1) {
                    outRect.setEmpty()
                } else {
                    super.getItemOffsets(outRect, view, parent, state)
                }
            }
        })
    }
}

inline fun RecyclerView.setOnScrollListener(crossinline cb: ((isScrollUp: Boolean) -> Unit)) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                cb.invoke(false)
            } else if (dy < 0) {
                cb.invoke(true)
            }
        }
    })
}

fun RecyclerView.setItemSlideUp(
    viewToAnimate: View,
    position: Int,
    lastPosition: Int
): Int {
    var animatePosition = lastPosition
    if (position > lastPosition) {
        viewToAnimate.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
        animatePosition = position
    }
    return animatePosition
}

/*recyclerview adapter*/
fun RecyclerView.Adapter<*>.handleStateLoading(
    items: MutableList<ItemViewType>,
    isLoading: Boolean
): Boolean {
    val loadMore = items.find { it is LoadingIVT }
    if (isLoading) {
        if (loadMore == null) {
            items.add(LoadingIVT())
            notifyItemInserted(itemCount)
        }
    } else {
        loadMore?.let {
            val index = items.indexOf(loadMore)
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }
    return isLoading
}

/*view group*/
//fun ViewGroup.hideSkeleton() {
//    if (this.tag is SkeletonScreen) {
//        val sk = this.tag as SkeletonScreen
//        sk.hide()
//        this.tag = null
//    }
//}
//
//fun ViewGroup.showSkeleton(@LayoutRes layoutResId: Int): SkeletonScreen {
//    hideSkeleton()
//    val sk = Skeleton.bind(this)
//        .load(layoutResId)
//        .color(R.color.color_sk)
//        .show()
//    this.tag = sk
//    return sk
//}
//
//fun ViewGroup.isShowSkeleton(): Boolean {
//    var isShow = false
//    if (this.tag is SkeletonScreen) {
//        isShow = true
//    }
//    return isShow
//}

/*ViewPager*/
fun ViewPager.getCurrentView(): View? {
    try {
        val currentItem = currentItem
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val layoutParams = child.layoutParams as ViewPager.LayoutParams
            val f: Field =
                layoutParams.javaClass.getDeclaredField("position") //NoSuchFieldException
            f.isAccessible = true
            val position = f.get(layoutParams) as Int //IllegalAccessException
            if (!layoutParams.isDecor && currentItem == position) {
                return child
            }
        }
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }
    return null
}

/*ViewPager2*/
fun ViewPager2.reduceDragSensitivity() {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this) as RecyclerView

    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView) as Int
    touchSlopField.set(recyclerView, touchSlop * 4)       // "4" was obtained experimentally
}

/*BottomNavigation*/
fun BottomNavigationView.disableTooltip() {
    menu.forEach {
        val view = findViewById<View>(it.itemId)
        view.setOnLongClickListener {
            true
        }
    }
}

fun BottomNavigationView.hideBottomNavMenuItem(menuId: Int) {
    val menuItem = this.menu.findItem(menuId)
    menuItem.isVisible = false
}

/*TabLayout*/
fun TabLayout.getSuggestTabMode(size: Int): Int {
    return if (size > 3) {
        TabLayout.MODE_SCROLLABLE
    } else {
        TabLayout.MODE_FIXED
    }
}

fun TabLayout.Tab.disableTooltip() {
    TooltipCompat.setTooltipText(view, null)
}

fun TabLayout.addItemMargin(dp: Float) {
    for (i in 0 until tabCount) {
        val tab = (getChildAt(0) as ViewGroup).getChildAt(i)
        val layoutParams = tab.layoutParams as ViewGroup.MarginLayoutParams
        val margin = convertDpToPx(resources, dp).roundToInt()
        val halfMargin = margin / 2
        when (i) {
            0 -> {
                layoutParams.setMargins(margin, halfMargin, halfMargin, halfMargin)
            }
            tabCount - 1 -> {
                layoutParams.setMargins(halfMargin, halfMargin, margin, halfMargin)
            }
            else -> {
                layoutParams.setMargins(halfMargin, halfMargin, halfMargin, halfMargin)
            }
        }
        tab.requestLayout()
    }
}

/*AppBarLayout*/
inline fun AppBarLayout.setOnExpandedListener(crossinline listener: (isExpanded: Boolean) -> Unit) {
    this.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
        if (verticalOffset == 0) {
            //Expanded
            listener.invoke(true)
        } else if (kotlin.math.abs(verticalOffset) - appBarLayout!!.totalScrollRange == 0) {
            //Collapsed
            listener.invoke(false)
        }
    }
}

//fun Button.disable() {
//    this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.alto))
//    this.isEnabled = false
//}
//
//fun Button.enable(@ColorRes color: Int = R.color.colorPrimary) {
//    this.setBackgroundColor(ContextCompat.getColor(this.context, color))
//    this.isEnabled = true
//}

/*WebView*/
@SuppressLint("SetJavaScriptEnabled")
fun WebView.loadHtmlView(data : String){
    this.apply {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            minimumFontSize = 18
        }
        loadDataWithBaseURL(null, data, "text/html", "UTF-8", null)
    }
}


fun View.show(){
    this.visibility = View.VISIBLE
}
fun View.gone() {
    this.visibility = View.GONE
}

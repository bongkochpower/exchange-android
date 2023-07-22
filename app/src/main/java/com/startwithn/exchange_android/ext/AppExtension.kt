package com.startwithn.exchange_android.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.icu.text.CompactDecimalFormat
import android.net.Uri
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.gms.common.util.Base64Utils
import com.startwithn.exchange_android.common.manager.AppManager
import com.startwithn.exchange_android.common.navigator.AppNavigator
import java.math.RoundingMode
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.and

/*activity*/
fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

fun Activity.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

@SuppressLint("ClickableViewAccessibility")
fun Activity.overrideOnViewTouchHideKeyBoard(view: View) {

    // Set up touch listener for non-text box views to hide keyboard.
    if (view !is EditText) {
        view.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            overrideOnViewTouchHideKeyBoard(innerView)
        }
    }
}

fun Activity.logout() {
    AppManager(this).removeAll {
        AppNavigator(this).goToLogin(true)
    }
}

fun Activity.finishWithOutAnimation() {
    finish()
    //Cancels animation on finish()
    overridePendingTransition(0, 0)
}

/*fragment*/
fun Fragment.hideKeyboard() {
    val activity = this.activity
    if (activity is Activity) {
        activity.hideKeyboard()
    }
}

fun Fragment.showKeyboard() {
    val activity = this.activity
    if (activity is Activity) {
        activity.showKeyboard()
    }
}

/*calculate*/
fun convertDpToPx(resource: Resources, dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resource.displayMetrics
    )
}

fun Activity.getNavigationBarHeight(): Int {
    val resources: Resources = resources
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0
}

fun Activity.getStatusBarHeight(): Int {
    val resources: Resources = resources
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0
}

/*convert*/
fun Int.toTimeStringFormat(): String = DecimalFormat("00").format(this)

fun Double.toStringFormat(isDecimal: Boolean = true): String {
    return if (isDecimal) {
        DecimalFormat("#,##0.00").format(this)
    } else {
        DecimalFormat("#,##0").format(this)
    }
}

fun Double.toStringFormat(): String = DecimalFormat("#,##0.00").format(this)

fun Double.toPointStringFormat(): String = DecimalFormat("#,##0.#").format(this)

fun Double.toThaiCurrencyStringFormat(): String = String.format("฿%s", toStringFormat(true))

fun Double.toCompactDecimalFormat() : String {
    val cdf = CompactDecimalFormat.getInstance(Locale.getDefault(), CompactDecimalFormat.CompactStyle.SHORT)
    return cdf.format(this)
}

fun String.toTimeAgo(format: String): String {
    val offerTime = this.toCalendar(format)
    val now = getCalendar()

    val value: Int
    val unit: String

    val year = now.get(Calendar.YEAR) - offerTime.get(Calendar.YEAR)
    val month = now.get(Calendar.MONTH) - offerTime.get(Calendar.MONTH)
    val day = now.get(Calendar.DATE) - offerTime.get(Calendar.DATE)
//    val hour = now.get(Calendar.HOUR) - offerTime.get(Calendar.HOUR)
//    val minute = now.get(Calendar.MINUTE) - offerTime.get(Calendar.MINUTE)

    when {
        year > 0 -> {
            value = year
//            unit = "year ago"
            unit = "ปีที่แล้ว"
        }
        month > 0 -> {
            value = month
//            unit = "month ago"
            unit = "เดือนที่แล้ว"
        }
        day > 0 -> {
            value = day
//            unit = "day ago"
            unit = "วันที่แล้ว"
        }
//        hour > 0 -> {
//            value = hour
//            unit = "hour ago"
//        }
//        minute > 0 -> {
//            value = minute
//            unit = "minute ago"
//        }
//        else -> return "just now"
        else -> return ""
    }
    return String.format("%s %s", value.toDouble().toStringFormat(), unit)
}



fun String?.toDashWhenNullOrEmpty(): String =
    if (!this.isNullOrEmpty()) {
        this
    } else {
        "-"
    }

fun String?.toCensorText(countOfShow: Int = 3): String =
    if (!this.isNullOrEmpty()) {
        val source = this.toString()
        val maxLength = source.length
        var result = ""
        if (maxLength > countOfShow) {
            val countOfCensor = maxLength - countOfShow
            for (i in 1..countOfCensor) {
                result += "*"
            }
            result += source.substring(countOfCensor - 1, maxLength - 1)
        } else {
            for (i in 1..maxLength) {
                result += "*"
            }
        }
        result
    } else {
        this.toString()
    }

fun String?.toAbbreviationText(): String =
    if (!this.isNullOrEmpty()) {
        var result = this.toString()
        if (result.isNotEmpty()) {
            val splitString = result.split(" ")
            if (splitString.size > 1) {
                result = splitString.fold("") { sum, word -> sum + word[0] }
            }
        }
        result
    } else {
        this.toString()
    }

fun String.base64ToBytes(): ByteArray {
    return Base64Utils.decode(this)
}

fun String.hexDecimalToString(): String {
    val output = StringBuilder()
    var i = 0
    while (i < this.length) {
        val str: String = this.substring(i, i + 2)
        output.append(str.toInt(16).toChar())
        i += 2
    }
    return output.toString().trim()
}

fun String.toHtmlWebViewFormat(): String {
    val text = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/db_heavent_regular.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;} ul { margin-left:-12px;} ol { margin-left:-12px;}</style><meta name=\"viewport\" content=\"width=device-width\"></head><body>"
    val pas = "</body></html>"
    return "$text$this$pas"
}

fun String.toSHA256(): String? {
    val md = MessageDigest.getInstance("SHA-256")
    md.update(this.toByteArray())
    return md.digest().bytesToHex()
}

private fun ByteArray.bytesToHex(): String? {
    val result = StringBuffer()
    for (b in this) {
        result.append(((b and 0xff.toByte()) + 0x100).toString(16).substring(1))
    }
    return result.toString()
}

fun ByteArray.bytesToString(): String {
    return String(this, StandardCharsets.UTF_8)
}

fun isAppRunning(context: Context, packageName: String?): Boolean {
    val activityManager: ActivityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val processInfoList: List<ActivityManager.RunningAppProcessInfo> =
        activityManager.runningAppProcesses
    for (processInfo in processInfoList) {
        if (processInfo.processName == packageName) {
            return true
        }
    }
    return false
}

fun goToGoogleStore(context: Context) {
    val appPackageName = context.packageName
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            )
        )
    } catch (anfe: android.content.ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}

//fun goToGoogleMapDirection(context: Context, latLng: LatLng) {
//    val intent = Intent(
//        Intent.ACTION_VIEW,
//        Uri.parse("http://maps.google.com/maps?daddr=" + latLng.latitude.toString() + "," + latLng.longitude)
//    )
//    context.startActivity(intent)
//}

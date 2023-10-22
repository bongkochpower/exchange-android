package com.powersoftlab.exchange_android.ext

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import com.powersoftlab.exchange_android.common.constant.AppConstant
import com.powersoftlab.exchange_android.common.constant.AppConstant.FORMAT_SERVICE_DATE_TIME
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun getCalendar(): Calendar = Calendar.getInstance(Locale.US)

fun Date?.toCalendar(): Calendar {
    val cal = getCalendar()
    if (this != null) {
        cal.time = this
    }
    return cal
}

fun Calendar.toString(format: String): String =
    SimpleDateFormat(format, Locale.US).format(this.time)

fun String.toCalendar(format: String): Calendar =
    SimpleDateFormat(format, Locale.US).parse(this).toCalendar()

fun getDiffYears(first: Calendar, last: Calendar): Int {
    var diff = last[Calendar.YEAR] - first[Calendar.YEAR]
    if (first[Calendar.MONTH] > last[Calendar.MONTH] || first[Calendar.MONTH] == last[Calendar.MONTH] && first[Calendar.DATE] > last[Calendar.DATE]) {
        diff -= 1
    }
    return diff
}

fun Context.getDatePickerDialog(
    cb: (date: String) -> Unit,
    calendar: Calendar = getCalendar(),
    isMaxToday: Boolean = false,
    year : Int = calendar.get(Calendar.YEAR),
    month : Int = calendar.get(Calendar.MONTH),
    day : Int = calendar.get(Calendar.DAY_OF_MONTH),
): DatePickerDialog {
    val datePickerDialog = DatePickerDialog(
        this,
        { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = monthOfYear
            calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            cb.invoke(calendar.toString(AppConstant.FORMAT_UI_DATE))
        },
        year,
        month,
        day
    )

    if (isMaxToday) {
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
    }
    return datePickerDialog
}

fun String?.reDateFormat(
    sourceFormat: String,
    destinationFormat: String,
    locale: Locale = Locale.US
): String? {
    return try {
        val sdfSource = SimpleDateFormat(sourceFormat, Locale.US)
        val sdfDestination = SimpleDateFormat(destinationFormat, locale)
        sdfDestination.format(sdfSource.parse(this!!)!!)
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }
}

fun String?.toDate(format: String): Date? {
    return try {
        SimpleDateFormat(format, Locale.US).parse(this!!)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun String?.toServiceFormat() : String{
    return reDateFormat(AppConstant.FORMAT_UI_DATE, AppConstant.FORMAT_SERVICE_DATE).toDashWhenNullOrEmpty()
}


fun String?.toDisplayFormat() : String{
    return reDateFormat(AppConstant.FORMAT_SERVICE_DATE,AppConstant.FORMAT_UI_DATE).toDashWhenNullOrEmpty()
}



fun String?.convertUtcToIct(): String? {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        val date = sdf.parse(this)
        val outputSdf = SimpleDateFormat(FORMAT_SERVICE_DATE_TIME,Locale.getDefault())
        outputSdf.timeZone = TimeZone.getTimeZone("Asia/Bangkok")
        outputSdf.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Context.getTimeZoneOffset() : String{
    val calendar = Calendar.getInstance(
        TimeZone.getTimeZone("GMT"),
        Locale.getDefault()
    )
    val currentLocalTime: Date = calendar.time
    val date: DateFormat = SimpleDateFormat("ZZZZZ")
    val localTime: String = date.format(currentLocalTime)
    return localTime
}


private const val diffYear: Int = 543
fun Int?.convertToBuddhistYear(): Int? = this?.plus(diffYear) ?: run { this }
fun Int?.convertToChristianYear(): Int? = this?.minus(diffYear) ?: run { this }

fun String?.convertDisplayDateToChristianYear(): String? =
    this?.let {
        val date = this.reDateFormat(
            AppConstant.FORMAT_UI_DATE,
            AppConstant.FORMAT_UI_DATE_MONTH_2
        )
        val year =
            this.toDate(AppConstant.FORMAT_UI_DATE).toCalendar()
                .get(
                    Calendar.YEAR
                ).convertToChristianYear()

        "$date/$year"
    } ?: run {
        this
    }

fun String?.convertDisplayDateToBuddhistYear(): String? =
    this?.let {
        val date = this.reDateFormat(
            AppConstant.FORMAT_UI_DATE,
            AppConstant.FORMAT_UI_DATE_MONTH_2
        )
        val year =
            this.toDate(AppConstant.FORMAT_UI_DATE).toCalendar()
                .get(
                    Calendar.YEAR
                ).convertToBuddhistYear()

        "$date/$year"
    } ?: run {
        this
    }
package com.powersoftlab.exchange_android.common.other

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import com.powersoftlab.exchange_android.ext.toDashWhenNullOrEmpty
import com.powersoftlab.exchange_android.ext.toPointStringFormat
import com.powersoftlab.exchange_android.ext.toStringFormat
import com.powersoftlab.exchange_android.common.constant.AppConstant
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object AppUtil {

    /*validation*/
    @JvmStatic
    fun isPhoneNumber(phoneNumber: String): Boolean =
        phoneNumber.trim().isNotBlank()
                && phoneNumber.length == AppConstant.PHONE_NUMBER_LENGTH
                && phoneNumber.first().toString() == "0"


    @JvmStatic
    fun isPasswordInvalid(password: String): Boolean =
        password.trim().length < AppConstant.MIN_PASSWORD

    @JvmStatic
    fun isEmail(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    @JvmStatic
    fun isMatch(compare1: String, compare2: String): Boolean =
        compare1 == compare2

    /*convert*/
    @JvmStatic
    fun convertIntToString(value: Int): String = value.toDouble().toStringFormat(false)

    @JvmStatic
    fun convertDoubleToPointFormat(value: Double): String = value.toPointStringFormat()

    /*@JvmStatic
    fun convertDoubleToThaiCurrencyFormat(value: Double): String = value.toStringThaiCurrencyFormat()*/

    @JvmStatic
    fun convertStringIsNullOrEmptyToDash(value: String?): String = value.toDashWhenNullOrEmpty()

    /*@JvmStatic
    fun convertStringToTimeFormat(value: String?): String = value.reDateFormat(
        AppConstant.FORMAT_SERVICE_DATE_TIME,
        AppConstant.FORMAT_UI_TIME
    ) ?: ""*/

    /*other*/
    @JvmStatic
    fun saveBitmap(context: Context, bitmap: Bitmap): File {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val fileName = String.format("%s_%d.jpg", "Image", System.currentTimeMillis())
        val outputFile = File(
            context.externalMediaDirs.first().absolutePath
                .toString() + File.separator + fileName
        )
        outputFile.createNewFile()
        val fileOutputStream = FileOutputStream(outputFile)
        fileOutputStream.write(bytes.toByteArray())
        fileOutputStream.close()
        return outputFile
    }

    @JvmStatic
    fun File.scanImageToGallery(context: Context) {
        MediaScannerConnection.scanFile(context, Array(1) { this.toString() }, null, null)
    }

}
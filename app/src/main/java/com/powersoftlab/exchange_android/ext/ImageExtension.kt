package com.powersoftlab.exchange_android.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.View
import android.view.Window
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.databinding.BindingAdapter
import androidx.documentfile.provider.DocumentFile
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.dhaval2404.imagepicker.ImagePicker
import com.powersoftlab.exchange_android.R
import com.powersoftlab.exchange_android.common.other.AppUtil.scanImageToGallery
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

const val IMAGE_RESULT_MAX_SIZE = 800
const val IMAGE_COMPRESS_MAX_SIZE = 400

private fun getFileName(): String = String.format("%s_%d.png", "Image", System.currentTimeMillis())

//fun Activity.takePhoto(activityResultLauncher: ActivityResultLauncher<Intent>) {
fun Activity.takePhoto(completionHandler: ((resultCode: Int, data: Intent?) -> Unit)? = null) {
    ImagePicker.with(this)
//            .crop(IMAGE_CROP_RATIO_X, IMAGE_CROP_RATIO_Y)
        .cameraOnly()
        .compress(IMAGE_COMPRESS_MAX_SIZE)
        .maxResultSize(IMAGE_RESULT_MAX_SIZE, IMAGE_RESULT_MAX_SIZE)
//        .createIntent { intent ->
//            activityResultLauncher.launch(intent)
//        }
        .start(completionHandler)
}

//fun Activity.takePhoto(activityResultLauncher: ActivityResultLauncher<Intent>) {
fun Activity.getPhotoFromGallery(completionHandler: ((resultCode: Int, data: Intent?) -> Unit)? = null) {
    ImagePicker.with(this)
//            .crop(IMAGE_CROP_RATIO_X, IMAGE_CROP_RATIO_Y)
        .galleryOnly()
        .galleryMimeTypes(  //Exclude gif images
            mimeTypes = arrayOf(
                "image/png",
                "image/jpg",
                "image/jpeg"
            )
        )
        .compress(IMAGE_COMPRESS_MAX_SIZE)
        .maxResultSize(IMAGE_RESULT_MAX_SIZE, IMAGE_RESULT_MAX_SIZE)
//        .createIntent { intent ->
//            activityResultLauncher.launch(intent)
//        }
        .start(completionHandler)
}

fun Activity.getFile(completionHandler: ((resultCode: Int, data: Intent?) -> Unit)? = null) {
    ImagePicker.with(this)
        .galleryOnly()
        .galleryMimeTypes(  //Exclude gif images
            mimeTypes = arrayOf(
                "application/pdf"
            )
        )
        .compress(IMAGE_COMPRESS_MAX_SIZE)
        .start(completionHandler)
}

fun Uri.uriToFile(context: Context): File {
    val fileExtension = getFileExtension(context, this)
    val fileName = DocumentFile.fromSingleUri(context, this)?.name ?: "temp_file.$fileExtension"

    val file = File(context.cacheDir, fileName)
    try {
        val inputStream = context.contentResolver.openInputStream(this)
        val outputStream = FileOutputStream(file)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream!!.read(buf).also { len = it } > 0) {
            outputStream.write(buf, 0, len)
        }
        outputStream.close()
        inputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}
private fun getFileExtension(context: Context, uri: Uri): String? {
    val fileType: String? = context.contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType) ?: "jpg"
}

private fun Context.getOutputDirectory(): File {
    val mediaDir = externalMediaDirs.firstOrNull()?.let {
        File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists()) {
        mediaDir
    } else {
        filesDir
    }
}

inline fun Context.saveImageFromUrl(
    url: String,
    crossinline callback: ((isSuccess: Boolean) -> Unit)
) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                resource.saveBitmap(this@saveImageFromUrl)
                callback.invoke(true)
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                callback.invoke(false)
            }
        })
}

fun Uri.toBitmap(context: Context): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val src: ImageDecoder.Source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(src)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    }
}

fun Bitmap.rotate(angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.resizedBitmap(maxSize: Int): Bitmap {
    var width = this.width
    var height = this.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    width = maxSize
    height = (width / bitmapRatio).toInt()
    return Bitmap.createScaledBitmap(this, width, height, true)
}

fun Bitmap.shareImage(context: Context) {
    //---Save bitmap to external cache directory---//
    //get cache directory
    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs()

    //create file
    val file = File(cachePath, "share_image.png")
    val fileOutputStream: FileOutputStream

    try {
        fileOutputStream = FileOutputStream(file)
        compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    //---Share File---//
    //get file uri
    val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)

    //create a intent
    val intent = Intent(Intent.ACTION_SEND).apply {
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/png"
    }
    context.startActivity(Intent.createChooser(intent, "Share with"))
}

fun Bitmap.saveBitmap(
    context: Context,
    outputFile: File = File(context.getOutputDirectory().absolutePath + File.separator + getFileName())
): File {
    val bytes = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, bytes)
    outputFile.createNewFile()
    val fileOutputStream = FileOutputStream(outputFile)
    fileOutputStream.write(bytes.toByteArray())
    fileOutputStream.close()
    outputFile.scanImageToGallery(context)
    return outputFile
}

private fun File.scanImageToGallery(context: Context) {
    MediaScannerConnection.scanFile(context, arrayOf(this.toString()), null, null)
}

inline fun View.captureView(window: Window, crossinline bitmapCallback: (Bitmap) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Above Android O, use PixelCopy
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val location = IntArray(2)
        this.getLocationInWindow(location)
        PixelCopy.request(
            window,
            Rect(location[0], location[1], location[0] + this.width, location[1] + this.height),
            bitmap,
            {
                if (it == PixelCopy.SUCCESS) {
                    bitmapCallback.invoke(bitmap)
                }
            },
            Handler(Looper.getMainLooper())
        )
    } else {
        val tBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.RGB_565)
        val canvas = Canvas(tBitmap)
        this.draw(canvas)
        canvas.setBitmap(null)
        bitmapCallback.invoke(tBitmap)
    }
}
inline fun View.createBitmapFromView(crossinline bitmapCallback: (Bitmap) -> Unit) {
    val tBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.RGB_565)
    val canvas = Canvas(tBitmap)
    this.draw(canvas)
    canvas.setBitmap(null)
    bitmapCallback.invoke(tBitmap)
}

fun String?.getMimeType(): MediaType? {
    var type: String? = "image/png"
    val extension = MimeTypeMap.getFileExtensionFromUrl(this)
    if (extension != null) {
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)?.let {
            type = it
        }
    }
    return type?.toMediaTypeOrNull()
}

/*image view*/
fun ImageView.loadImage(resId: Int?) {
    try {
        Glide.with(context).load(resId).into(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun ImageView.loadImage(uri: Uri?) {
    try {
        Glide.with(context).load(uri).into(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter(value = ["setImageUrl"])
fun ImageView.loadImage(url: String?) {
    try {
        Glide.with(context).load(url).into(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun ImageView.loadImageCircle(uri: Uri?) {
    try {
        Glide.with(context).load(uri).apply(RequestOptions().circleCrop()).into(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter(value = ["setImageCircleUrl"])
fun ImageView.loadImageCircle(url: String?) {
    try {
        Glide.with(context).load(url).apply(RequestOptions().circleCrop())
            .error(R.drawable.bg_image_profile).into(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
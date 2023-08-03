package com.powersoftlab.exchange_android.common.manager

import android.app.*
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.powersoftlab.exchange_android.BuildConfig
import com.powersoftlab.exchange_android.R
import me.leolin.shortcutbadger.ShortcutBadger
import java.io.IOException
import java.net.URL

class NotificationManager {
    companion object {

        private fun getNotificationBuilder(
            context: Context,
            title: String?,
            message: String?,
            image: Bitmap?,
            pendingIntent: PendingIntent
        ): Notification {
            val builder =
                NotificationCompat.Builder(context, context.getString(R.string.fcm_channel_id))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setLights(ContextCompat.getColor(context, R.color.black), 1000, 300)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    //.setSmallIcon(R.drawable.ic_notification)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setGroup(BuildConfig.APPLICATION_ID)

            return if (image == null) {
                builder.build()
            } else {
                builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(image)).build()
            }
        }

        private fun getGroupNotificationBuilder(
            context: Context,
            title: String?,
            message: String?,
            pendingIntent: PendingIntent
        ): Notification {
            val builder =
                NotificationCompat.Builder(context, context.getString(R.string.fcm_channel_id))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    //.setSmallIcon(R.drawable.ic_notification)
                    .setGroup(BuildConfig.APPLICATION_ID)
                    .setGroupSummary(true)

            return builder.build()
        }

        private fun getNotificationManager(context: Context): NotificationManager {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    context.getString(R.string.fcm_channel_id),
                    context.getString(R.string.fcm_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.apply {
                    description = context.getString(R.string.fcm_channel_desc)
                    setShowBadge(true)
                    enableLights(true)
                    lightColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    enableVibration(true)
                    vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }

                notificationManager.createNotificationChannel(channel)
            }

            return notificationManager
        }

        fun getIntent(context: Context, action: String, actionParam: String): Intent {
            return context.packageManager.getLaunchIntentForPackage(context.packageName)!!
        }

        fun sendNotification(
            context: Context,
            title: String?,
            body: String?,
            image: String?,
            intent: Intent
        ) {

            // Check if app is in background
            val isInBG =
                ProcessLifecycleOwner.get().lifecycle.currentState == Lifecycle.State.CREATED
            // Check if app is in foreground
            val isInFG =
                ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

            val pendingIntent: PendingIntent = if (isInBG) TaskStackBuilder.create(context).run {
                // Add the intent, which inflates the back stack
                addNextIntentWithParentStack(intent)
                // Get the PendingIntent containing the entire back stack
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                val pendingIntent: PendingIntent =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        PendingIntent.getActivity(
                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
                        )
                    } else {
                        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
                    }
                pendingIntent
            }


            var bitmap: Bitmap? = null
            try {
                if (!image.isNullOrEmpty()) {
                    val url = URL(image)
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val notifyId = System.currentTimeMillis().toInt()
            getNotificationManager(context).apply {
                notify(
                    notifyId,
                    getNotificationBuilder(context, title, body, bitmap, pendingIntent)
                )

                notify(0, getGroupNotificationBuilder(context, title, body, pendingIntent))
            }

            if (ShortcutBadger.isBadgeCounterSupported(context)) ShortcutBadger.applyCountOrThrow(context, 1)

        }

        fun resetBadgeCounterMessages(context: Context) {
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
            ShortcutBadger.removeCount(context)
        }
    }
}

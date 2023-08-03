package com.powersoftlab.exchange_android.firebase.notification

import com.powersoftlab.exchange_android.common.enum.AppEventEnum
import com.powersoftlab.exchange_android.common.manager.AppManager
import com.powersoftlab.exchange_android.common.rx.RxBus
import com.powersoftlab.exchange_android.common.rx.RxEvent
import com.powersoftlab.exchange_android.ext.isAppRunning
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.powersoftlab.exchange_android.common.manager.NotificationManager
import org.koin.android.ext.android.inject

class AppFCM : FirebaseMessagingService() {

    private val appManager: AppManager by inject()

    companion object {

    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        appManager.setFcmToken(newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

//        if (appManager.isOpenNotification()) { /*manage from service*/
            remoteMessage.notification?.let {
                sendNotification(it)
            } ?: run {
                sendNotification(remoteMessage.data)
            }

            appManager.addNotificationCount()

            if (isAppRunning(this, packageName)) {
                RxBus.publish(RxEvent.AppEvent(AppEventEnum.NOTIFY_UPDATE, null))
            }
//        }
    }

    private fun sendNotification(notification: RemoteMessage.Notification) {
        NotificationManager.sendNotification(
            this,
            notification.title,
            notification.body,
            null,
            NotificationManager.getIntent(this, "", "")
        )
    }

    private fun sendNotification(data: Map<String, String>) {

        val action = data["type"].toString()
        val actionParam = data["action_id"].toString()

        NotificationManager.sendNotification(
            this,
            data["title"],
            data["body"],
            data["image"],
            NotificationManager.getIntent(this, action, actionParam)
        )

        if (isAppRunning(this, packageName)) {
            RxBus.publish(RxEvent.AppEvent(AppEventEnum.NOTIFY_UPDATE, null))
        }
    }
}
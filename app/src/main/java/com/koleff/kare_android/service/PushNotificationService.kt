package com.koleff.kare_android.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.koleff.kare_android.common.NotificationManager

class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("FCM", "New token registered: $token")
        super.onNewToken(token)

        //TODO: save to DB -> link to user...
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "From: ${remoteMessage.from}")

        //Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM", "Message data payload: ${remoteMessage.data}")
        }

        //Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")

            val title = it.title ?: ""
            val body = it.body ?: ""

            val context = this@PushNotificationService
            NotificationManager.sendNotification(context, title, body)
        }

        super.onMessageReceived(remoteMessage)
    }
}
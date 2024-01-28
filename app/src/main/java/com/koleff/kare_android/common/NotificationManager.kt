package com.koleff.kare_android.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.koleff.kare_android.R

object NotificationManager {

    fun sendTestNotification(context: Context) {
        // 'notificationId' is a unique int for each notification that you must define
        val notificationId = 1 // For testing, you can use a fixed ID.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setContentTitle("Test Notification")
                .setContentText("This is a sample notification using the channel.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_WORKOUT)
                .setAutoCancel(false)
                .setOngoing(false)

            with(NotificationManagerCompat.from(context)) {
                val activity = context as? Activity ?: return

                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }

                notify(notificationId, builder.build())
            }
        }
    }
}
package com.koleff.kare_android.common

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.MainActivity

object NotificationManager {

    fun sendTestNotification(context: Context) {
        // 'notificationId' is a unique int for each notification that you must define
        val notificationId = 1 // For testing, you can use a fixed ID.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Test Notification")
                .setContentText("This is a sample notification using the channel.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_WORKOUT)
                .setAutoCancel(false)
                .setOngoing(false)

            with(NotificationManagerCompat.from(context)) {
                val activity = context as? Activity ?: return

                //Check for permission
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

    fun openNotificationSettings(context: Context) {
        val packageName = context.packageName

        val intent = Intent().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }
        }
        startActivity(context, intent, null)
    }
}
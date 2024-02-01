package com.koleff.kare_android.common

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.MainActivity
import kotlinx.coroutines.tasks.await


object NotificationManager {

    fun sendNotification(context: Context, title: String, message: String) {
        //Check for notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        val notificationId = 1 //'notificationId' is a unique int for each notification that you must define. For testing, you can use a fixed ID.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Ringtone sound
            val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            //Vibration
            val pattern = longArrayOf(500, 500, 500, 500, 500)

            var pendingIntent: PendingIntent? = null
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //Pop up notification
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_MUTABLE //FLAG_UPDATE_CURRENT
                )
            }

            val builder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_WORKOUT)
                .setSound(defaultSound)
                .setVibrate(pattern)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(false)

            with(NotificationManagerCompat.from(context)) {
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

    fun createNotificationChannel(context: Context) = with(context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                "Push notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    suspend fun subscribeToTopic() {
        Firebase.messaging.subscribeToTopic("Gym").await()
    }
}
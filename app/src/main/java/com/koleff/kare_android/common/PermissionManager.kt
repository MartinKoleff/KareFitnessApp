package com.koleff.kare_android.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat


object PermissionManager {

    fun requestNotificationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val activity = context as? Activity
                ?: return false //Return if the context is not an instance of Activity

            val hasPermission = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            //Prompt user to enable push notifications
            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        } else {
            //Old version -> show custom prompt
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled()

            //Show dialog
            if (!areNotificationsEnabled) {
                return true
            }
        }

        return false
    }

    fun hasNotificationPermission(context: Context): Boolean {
        val activity = context as? Activity
            ?: return false //Return if the context is not an instance of Activity

        //Check for permission
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            //Old version
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled()
            return areNotificationsEnabled
        }
    }
    }
}
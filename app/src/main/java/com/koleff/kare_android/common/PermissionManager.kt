package com.koleff.kare_android.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

object PermissionManager {

    fun requestNotificationPermission(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val activity = context as? Activity ?: return  //Return if the context is not an instance of Activity

            val hasPermission = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

            //Prompt user to enable push notifications
            if(!hasPermission){
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }else{
            //TODO: Custom prompt for older version...
        }
    }
}
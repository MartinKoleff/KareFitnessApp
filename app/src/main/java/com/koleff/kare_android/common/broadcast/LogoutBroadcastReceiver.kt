package com.koleff.kare_android.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import javax.inject.Inject

class LogoutBroadcastReceiver @Inject constructor(
    private val logoutHandler: LogoutHandler
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("LogoutBroadcastReceiver", "Logout broadcast received.")
        logoutHandler.logout(context)
    }
}
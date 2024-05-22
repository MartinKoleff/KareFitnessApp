package com.koleff.kare_android.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import javax.inject.Inject

class RegenerateTokenBroadcastReceiver @Inject constructor(
    private val regenerateTokenHandler: RegenerateTokenHandler
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("RegenerateTokenBroadcastReceiver", "Regenerate token broadcast received.")
        regenerateTokenHandler.regenerateToken()
    }
}
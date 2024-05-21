package com.koleff.kare_android.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class RegenerateTokenBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var authenticationUseCases: AuthenticationUseCases

    @Inject
    lateinit var preferences: Preferences
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("RegenerateTokenBroadcastReceiver", "Regenerate token broadcast received.")

        val tokens = preferences.getTokens() ?: return

        CoroutineScope(Dispatchers.IO).launch {
            authenticationUseCases.regenerateTokenUseCase.invoke(tokens)
                .collect { regenerateTokenState ->
                    if (regenerateTokenState.isSuccessful) {
                        Log.d("RegenerateTokenBroadcastReceiver", "Regenerate token successfully done!")

                        //Update preferences with new tokens
                        preferences.updateTokens(regenerateTokenState.tokens)
                    } else if (regenerateTokenState.isError) {
                        Log.d("RegenerateTokenBroadcastReceiver", "Regenerate token failed!")
                        Log.d("RegenerateTokenBroadcastReceiver", "Logging out...")

                        //Logout
                        preferences.deleteTokens()

                        //Send logout broadcast
                    }
                }
        }
    }
}
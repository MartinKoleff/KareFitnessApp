package com.koleff.kare_android.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.koleff.kare_android.common.network.RegenerateTokenNotifier
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.ui.state.TokenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RegenerateTokenBroadcastReceiver @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
    private val preferences: Preferences
) : BroadcastReceiver(), RegenerateTokenNotifier {

    private val _regenerateTokenState = MutableSharedFlow<TokenState>(replay = 1)
    override val regenerateTokenState: Flow<TokenState> = _regenerateTokenState.asSharedFlow()

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("RegenerateTokenBroadcastReceiver", "Regenerate token broadcast received.")

        val tokens = preferences.getTokens()

        CoroutineScope(Dispatchers.IO).launch {

            //Validation
            tokens ?: run {
                _regenerateTokenState.emit(
                    TokenState(
                        isError = true,
                        error = KareError.INVALID_TOKEN
                    )
                )

                return@launch
            }

            authenticationUseCases.regenerateTokenUseCase.invoke(tokens)
                .collect { regenerateTokenState ->
                    Log.d("RegenerateTokenBroadcastReceiver", "Regenerate token state received. State: $regenerateTokenState")
                    _regenerateTokenState.emit(regenerateTokenState)

                    if (regenerateTokenState.isSuccessful) {
                        Log.d("RegenerateTokenBroadcastReceiver", "Regenerate token successfully done!")

                        //Update preferences with new auth tokens
                        preferences.updateTokens(regenerateTokenState.tokens)
                    } else if (regenerateTokenState.isError) {
                        Log.d("RegenerateTokenBroadcastReceiver", "Regenerate token failed!")
                        Log.d("RegenerateTokenBroadcastReceiver", "Logging out...")

                        //Delete existing auth tokens
                        preferences.deleteTokens()
                    }
                }
        }
    }
}
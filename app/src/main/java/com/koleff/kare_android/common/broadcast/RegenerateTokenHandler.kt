package com.koleff.kare_android.common.broadcast

import android.util.Log
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

//TODO: remove dependencies causing circular dependency...
@Singleton
class RegenerateTokenHandler @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
    private val preferences: Preferences
): RegenerateTokenNotifier {

    private val _regenerateTokenState = MutableSharedFlow<TokenState>(replay = 1)
    override val regenerateTokenState: Flow<TokenState> = _regenerateTokenState.asSharedFlow()
    fun regenerateToken() {
        Log.d("RegenerateTokenHandler", "Regenerate token handler received.")

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
                    Log.d(
                        "RegenerateTokenHandler",
                        "Regenerate token state received. State: $regenerateTokenState"
                    )
                    _regenerateTokenState.emit(regenerateTokenState)

                    if (regenerateTokenState.isSuccessful) {
                        Log.d(
                            "RegenerateTokenHandler",
                            "Regenerate token successfully done!"
                        )

                        //Update preferences with new auth tokens
                        preferences.updateTokens(regenerateTokenState.tokens)
                    } else if (regenerateTokenState.isError) {
                        Log.d("RegenerateTokenHandler", "Regenerate token failed!")
                        Log.d("RegenerateTokenHandler", "Logging out...")

                        //Delete existing auth tokens
                        preferences.deleteTokens()
                    }
                }
        }
    }
}
package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants.splashScreenDelay
import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.ui.state.LoginState
import com.koleff.kare_android.ui.state.SplashScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
    private val preferences: Preferences,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(SplashScreenState())
    val state = _state.asStateFlow()

    private var _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    val hasOnboardedState: Boolean = hasOnboarded()

    init {
        getCredentials()
    }

    fun getCredentials() {
        viewModelScope.launch {
            delay(splashScreenDelay)

            val credentials = preferences.getCredentials()
            val tokens = preferences.getTokens()

            val hasCredentials = credentials != null //&& tokens != null
            Log.d("SplashScreenViewModel", "hasCredentials: $hasCredentials")
            Log.d("SplashScreenViewModel", "Credentials: $credentials")
            Log.d("SplashScreenViewModel", "Tokens: $tokens")

            if (hasCredentials) {
                loginWithCachedCredentials(credentials!!)
            } else {
                _state.value = SplashScreenState(
                    isSuccessful = true,
                    hasSignedIn = false
                )
            }
        }
    }

    private fun loginWithCachedCredentials(credentials: Credentials) {
        viewModelScope.launch(dispatcher) {
            authenticationUseCases.loginUseCase(credentials).collect { loginState ->
                _loginState.value = loginState

                if (loginState.isSuccessful) {
                    _state.value = SplashScreenState(
                        isSuccessful = true,
                        hasSignedIn = true,
                        credentials = credentials,
                        tokens = Tokens(
                            accessToken = loginState.data.accessToken,
                            refreshToken = loginState.data.refreshToken
                        )
                    )
                } else if (loginState.isError) {
                    //TODO: in future check session / refresh tokens...

                    _state.value = SplashScreenState(
                        isSuccessful = false,
                        hasSignedIn = false,
                        error = loginState.error,
                        isError = true,
                    )
                }
            }
        }
    }

    private fun hasOnboarded(): Boolean{
        return preferences.getHasOnboarded()
    }
}
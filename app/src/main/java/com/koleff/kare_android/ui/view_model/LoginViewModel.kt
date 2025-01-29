package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.ui.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigationController: NavigationController,
    private val authenticationUseCases: AuthenticationUseCases,
    private val preferences: Preferences,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController) {

    private var _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun login(credentials: Credentials) {
        viewModelScope.launch(dispatcher) {
            authenticationUseCases.loginUseCase.invoke(credentials)
                .collect { loginState ->

                    Log.d("LoginViewModel", "$loginState")
                    _state.value = loginState
                }
        }
    }

    override fun clearError() {
        if (state.value.isError) {
            _state.value = LoginState()
        }
    }

    fun navigateToDashboard() {
        onNavigationEvent(NavigationEvent.ClearBackstackAndNavigateTo(Destination.Dashboard))
    }

    fun saveCredentials() = with(state.value.data) {
        preferences.saveCredentials(user)

        saveTokens(accessToken, refreshToken)
    }

    private fun saveTokens(accessToken: String, refreshToken: String) {
        val tokens = Tokens(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
        preferences.saveTokens(tokens)
    }

    fun navigateToWelcome() {
        onNavigationEvent(NavigationEvent.NavigateBack)
    }

    fun navigateToSignUp(){
        onNavigationEvent(NavigationEvent.ClearBackstackAndNavigateTo(Destination.Register))
    }

    fun forgotPassword() {
        TODO("Not yet implemented")
    }
}
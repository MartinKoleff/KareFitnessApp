package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.credentials_validator.AuthenticationNotifier
import com.koleff.kare_android.common.credentials_validator.Credentials
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticator
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.ui.state.BaseState
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
    private val credentialsAuthenticator: CredentialsAuthenticator,
    private val authenticationNotifier: AuthenticationNotifier,
    private val authenticationUseCases: AuthenticationUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController) {

    private var _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    init {
        observeAuthenticationState()
    }

    private fun observeAuthenticationState() {
        viewModelScope.launch(dispatcher) {
            authenticationNotifier.authenticationState.collect { baseState ->

            }
        }
    }

    fun login(credentials: Credentials) {
        viewModelScope.launch(dispatcher) {
            validateCredentials(credentials)

            //On success...
            authenticationUseCases.loginUseCase.invoke(
                credentials.username,
                credentials.password
            ).collect { loginState ->
                _state.value = loginState
            }
        }
    }

    private suspend fun validateCredentials(credentials: Credentials) { //TODO: wire authentication state / emit it?
        credentialsAuthenticator.checkCredentials(credentials)
    }
}
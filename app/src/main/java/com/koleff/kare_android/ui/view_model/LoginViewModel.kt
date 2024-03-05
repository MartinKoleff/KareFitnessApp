package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.credentials_validator.AuthenticationNotifier
import com.koleff.kare_android.common.credentials_validator.Credentials
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticator
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigationController: NavigationController,
    private val credentialsAuthenticator: CredentialsAuthenticator,
    private val authenticationUseCases: AuthenticationUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController) {

    private var _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private var _credentialsAuthenticationState: MutableStateFlow<BaseState> = MutableStateFlow(
        BaseState()
    )
    private val credentialsAuthenticationState: StateFlow<BaseState> = _credentialsAuthenticationState

    fun login(credentials: Credentials) {
        viewModelScope.launch(dispatcher) {

            //Validate credentials
            credentialsAuthenticator.checkCredentials(credentials) //TODO: test if awaiting result...
                .collect { credentialsAuthenticationState ->
                    _credentialsAuthenticationState.value = credentialsAuthenticationState
                }

           if(credentialsAuthenticationState.value.isSuccessful){
               authenticationUseCases.loginUseCase.invoke(
                   credentials.username,
                   credentials.password
               ).collect { loginState ->
                   _state.value = loginState
               }
           }else if(credentialsAuthenticationState.value.isError){
               _state.value = LoginState(
                   isError = true,
                   error = KareError.INVALID_CREDENTIALS
               )
           }
        }
    }
}
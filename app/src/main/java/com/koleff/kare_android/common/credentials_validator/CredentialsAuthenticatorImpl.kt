package com.koleff.kare_android.common.credentials_validator

import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CredentialsAuthenticatorImpl @Inject constructor(
    private val credentialsValidator: CredentialsValidator,
    private val preferences: Preferences
) : CredentialsAuthenticator, AuthenticationNotifier {

    private val _authenticationState = MutableStateFlow(BaseState()) //TODO: wire with view model...

    override val authenticationState: StateFlow<BaseState> = _authenticationState.asStateFlow()

    override suspend fun checkCredentials(credentials: Credentials) {
        when (credentialsValidator.validate(credentials)){
            is ResultWrapper.ApiError -> {
                _authenticationState.update {
                    BaseState(
                        isError = true,
                        error = it.error
                    )
                }
            }

            is ResultWrapper.Loading -> {
                _authenticationState.update {
                    BaseState(
                        isLoading = true
                    )
                }
            }

            is ResultWrapper.Success -> {
                BaseState(
                    isSuccessful = true
                )

                //save credentials and login...
                saveCredentials(credentials)
            }
        }
    }

    override suspend fun saveCredentials(credentials: Credentials) {
        preferences.saveCredentials(credentials)
    }
}
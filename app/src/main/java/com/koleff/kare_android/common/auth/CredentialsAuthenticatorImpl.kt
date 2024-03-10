package com.koleff.kare_android.common.auth

import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CredentialsAuthenticatorImpl @Inject constructor(
    private val credentialsValidator: CredentialsValidator,
    private val credentialsDataStore: CredentialsDataStore
) : CredentialsAuthenticator {
    override suspend fun checkRegisterCredentials(credentials: Credentials): Flow<BaseState> = flow {
        when (val data = credentialsValidator.validateRegister(credentials)) {
            is ResultWrapper.ApiError ->
                emit(
                    BaseState(
                        isError = true,
                        error = data.error ?: KareError.GENERIC
                    )
                )

            is ResultWrapper.Loading ->
                emit(
                    BaseState(
                        isLoading = true
                    )
                )

            is ResultWrapper.Success -> {
                emit(
                    BaseState(
                        isSuccessful = true
                    )
                )
            }
        }
    }

    override suspend fun checkLoginCredentials(username: String, password: String): Flow<BaseState> = flow {
        when (val data = credentialsValidator.validateLogin(username, password)) {
            is ResultWrapper.ApiError ->
                emit(
                    BaseState(
                        isError = true,
                        error = data.error ?: KareError.GENERIC
                    )
                )

            is ResultWrapper.Loading ->
                emit(
                    BaseState(
                        isLoading = true
                    )
                )

            is ResultWrapper.Success -> {
                emit(
                    BaseState(
                        isSuccessful = true
                    )
                )
            }
        }
    }


    override suspend fun saveCredentials(credentials: Credentials) {
        credentialsDataStore.saveCredentials(credentials)
    }
}